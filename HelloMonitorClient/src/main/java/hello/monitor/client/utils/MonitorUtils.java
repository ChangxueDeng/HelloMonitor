package hello.monitor.client.utils;

import hello.monitor.client.entity.BaseDetail;
import hello.monitor.client.entity.RuntimeDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.*;

@Slf4j
@Component
public class MonitorUtils {
    private final SystemInfo info = new SystemInfo();
    private final Properties properties = System.getProperties();
    public BaseDetail monitorBaseDetail() {
        OperatingSystem os = info.getOperatingSystem();
        HardwareAbstractionLayer hardware = info.getHardware();
        double memory = hardware.getMemory().getTotal() / 1024.0 / 1024 / 1024;
        double diskSize = Arrays.stream(File.listRoots()).mapToLong(File::getTotalSpace).sum() / 1024.0 / 1024 / 1024;
        String ip = Objects.requireNonNull(this.findNetworkInterface(hardware)).getIPv4addr()[0];
        return new BaseDetail()
                .setOsArch(properties.getProperty("os.arch"))
                .setOsName(os.getFamily())
                .setOsVersion(os.getVersionInfo().getVersion())
                .setOsBit(os.getBitness())
                .setCpuName(hardware.getProcessor().getProcessorIdentifier().getName())
                .setCpuCore(hardware.getProcessor().getLogicalProcessorCount())
                .setMemory(memory)
                .setDisk(diskSize)
                .setIp(ip);
    }
    public RuntimeDetail monitorRuntimeDetail() {
        double statisticTime = 0.5;
        try {
            HardwareAbstractionLayer hardware = info.getHardware();
            NetworkIF networkInterFace = Objects.requireNonNull(this.findNetworkInterface(hardware));
            CentralProcessor processor = hardware.getProcessor();
            double upload = networkInterFace.getBytesSent(), download = networkInterFace.getBytesRecv();
            double read = hardware.getDiskStores().stream().mapToLong(HWDiskStore::getReadBytes).sum();
            double write = hardware.getDiskStores().stream().mapToLong(HWDiskStore::getReadBytes).sum();
            long[] systemCpuLoadTicks = processor.getSystemCpuLoadTicks();
            Thread.sleep((long) (statisticTime * 1000));
            networkInterFace = Objects.requireNonNull(this.findNetworkInterface(hardware));
            upload = (networkInterFace.getBytesSent() - upload) / statisticTime;
            download = (networkInterFace.getBytesRecv() - download) / statisticTime;
            read = (hardware.getDiskStores().stream().mapToLong(HWDiskStore::getReadBytes).sum() - read) / statisticTime;
            write = (hardware.getDiskStores().stream().mapToLong(HWDiskStore::getReadBytes).sum() - write) / statisticTime;
            double memory = (hardware.getMemory().getTotal() - hardware.getMemory().getAvailable()) / 1024.0 / 1024 / 1024;
            double disk = Arrays.stream(File.listRoots()).mapToLong(file -> file.getTotalSpace() - file.getFreeSpace()).sum() / 1024.0 / 1024 / 1024;
            return new RuntimeDetail()
                    .setCpuUsage(this.calculateCpuUsage(processor, systemCpuLoadTicks))
                    .setDiskUsage(disk)
                    .setDiskRead(read / 1024 / 1024)
                    .setDiskWrite(write / 1024 / 1024)
                    .setMemoryUsage(memory)
                    .setNetworkUpload(upload / 1024)
                    .setNetworkDownload(download / 1024)
                    .setTimestamp(System.currentTimeMillis());
        }catch (Exception e){
            log.error("读取运行时信息出错", e);
        }
        return null;
    }
    private double calculateCpuUsage(CentralProcessor processor, long[] prevTicks) {
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()]
                - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()]
                - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
                - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long cUser = ticks[CentralProcessor.TickType.USER.getIndex()]
                - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
                - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()]
                - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = cUser + nice + cSys + idle + ioWait + irq + softIrq + steal;
        return (cSys + cUser) * 1.0 / totalCpu;
    }

    private NetworkIF findNetworkInterface(HardwareAbstractionLayer hardware) {
        try {
            ArrayList<NetworkIF> preNet = new ArrayList<>();
            for (int i = 0; i < hardware.getNetworkIFs().size(); i++) {
                NetworkIF network = hardware.getNetworkIFs().get(i);
                String[] ipv4 = network.getIPv4addr();
                NetworkInterface ni = network.queryNetworkInterface();
                boolean check = ipv4.length > 0 && ni.isUp() && !ni.isVirtual() && !ni.isLoopback() && !ni.isVirtual() && !ni.isPointToPoint()
                        && (ni.getName().startsWith("wlan") || ni.getName().startsWith("eth") || ni.getName().startsWith("en"));
                if (check) {
                    //寻找接受数据最大的网卡
                    if (network.getBytesRecv() > 0|| network.getBytesSent() > 0) {
                        if (!preNet.isEmpty() && network.getBytesRecv() > preNet.get(0).getBytesRecv()) {
                            preNet.add(0, network);
                        }else {
                            preNet.add(network);
                        }
                    }
                    else {
                        preNet.add(network);
                    }
                }
            }
//            System.out.println("大小: " + preNet.size() + " : " + preNet.get(0));
            return preNet.get(0);
        }catch (IOException e) {
            log.error("读取网络接口信息出错", e);
        }
        return null;
    }
}
