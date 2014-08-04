package util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileSystemView;

import com.google.gson.Gson;

import model.Disk;
import model.PcFile;

/**
 * 文件的公共类
 * 
 * @author 20082755
 * 
 */
public class FileUtil {

	/**
	 * 文件大小格式化
	 * 
	 * @param fileS
	 * @return
	 */
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 获取硬盘的盘符
	 * 
	 * @return
	 */
	public static String getDriver() {
		List<Disk> disks = new ArrayList<Disk>();

		// 获得当前文件系统类
		FileSystemView systemView = FileSystemView.getFileSystemView();

		// 桌面
		/*Disk homeDisk = new Disk();

		File file = systemView.getHomeDirectory();

		homeDisk.setFileName(systemView.getSystemDisplayName(file));

		homeDisk.setTotalSpace(FormetFileSize(file.getTotalSpace()));

		homeDisk.setFreeSpace(FormetFileSize(file.getFreeSpace()));

		homeDisk.setPath(file.getAbsolutePath());

		disks.add(homeDisk);*/

		// 我的文档
		/*Disk myDisk = new Disk();

		File myFile = systemView.getDefaultDirectory();

		myDisk.setFileName(systemView.getSystemDisplayName(myFile));

		myDisk.setTotalSpace(FormetFileSize(myFile.getTotalSpace()));

		myDisk.setFreeSpace(FormetFileSize(myFile.getFreeSpace()));

		myDisk.setPath(myFile.getAbsolutePath());

		disks.add(myDisk);*/

		// 列出所有的windows 磁盘
		File[] files = File.listRoots();

		for (int i = 0; i < files.length; i++) {
			Disk disk = new Disk();

			disk.setFileName(systemView.getSystemDisplayName(files[i]));

			disk.setTotalSpace(FormetFileSize(files[i].getTotalSpace()));

			disk.setFreeSpace(FormetFileSize(files[i].getFreeSpace()));

			disk.setPath(files[i].getAbsolutePath());

			disks.add(disk);
		}

		Gson gson = new Gson();

		String jsonStr = gson.toJson(disks);

		return jsonStr;
	}

	/**
	 * @param dir表示需要指定的盘符，列出某目录下的所有文件
	 * 
	 */
	public static String getDirInTray(String path) {

		File dir = new File(path);

		List<PcFile> list = new ArrayList<PcFile>();

		// 获取指定盘符下的所有文件列表。（listFiles可以获得指定路径下的所有文件，以数组方式返回）
		File[] files = dir.listFiles();
		// 如果该目录下面为空，则该目录的此方法执行
		if (files == null) {
			return "";
		}// 通过循环将所遍历所有文件
		for (int i = 0; i < files.length; i++) {

			if (!files[i].isHidden()) {

				PcFile pcFile = new PcFile();

				pcFile.setFileName(files[i].getName());

				pcFile.setFilePath(files[i].getAbsolutePath());

				pcFile.setTotalSpace(FormetFileSize(files[i].getTotalSpace()));

				pcFile.setFreeSpace(FormetFileSize(files[i].getFreeSpace()));

				pcFile.setLength(files[i].length());

				pcFile.setDirectory(files[i].isDirectory());

				pcFile.setFile(files[i].isFile());

				pcFile.setParent(files[i].getParent());

				pcFile.setParentPath(files[i].getParentFile().getAbsolutePath());

				list.add(pcFile);
			}
		}
		Gson gson = new Gson();

		String jsonStr = gson.toJson(list);

		return jsonStr;
	}

}
