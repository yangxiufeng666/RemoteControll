package com.devil.remotecontroll.util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.devil.remotecontroll.R;

/**
 * 文件处理的工具类
 * 
 * @author 赵庆洋
 * 
 */
public class FileUtil {

	/**
	 * 计算目录大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		// 不是目录
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;

		File[] files = dir.listFiles();

		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				// 递归调用
				dirSize += getDirSize(file);
			}

		}
		return dirSize;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {

		if (fileS == 0) {
			return "0.00B";
		}

		DecimalFormat dFormat = new DecimalFormat("#.00");

		String fileSizeString = "";

		if (fileS < 1024) {
			fileSizeString = dFormat.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = dFormat.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = dFormat.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = dFormat.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * 文件目录地址
	 * 
	 * @return
	 */
	public static String fileDirectory(String dirPath, String fileName) {
		String filePath = "";

		String storageState = Environment.getExternalStorageState();

		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			filePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + dirPath;

			File file = new File(filePath);

			if (!file.exists()) {
				// 建立一个新的目录
				file.mkdirs();
			}
			filePath = filePath + fileName;
		}

		return filePath;
	}

	/**
	 * 获取文件目录
	 * 
	 * @return
	 */
	public static File getDirectoryFile(String dirPath) {

		String storageState = Environment.getExternalStorageState();

		File file = null;

		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			String filePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + dirPath;

			file = new File(filePath);

			if (!file.exists()) {
				// 建立一个新的目录
				file.mkdirs();
			}
		}

		return file;
	}

	/**
	 * 检查文件后缀
	 * 
	 * @param checkItsEnd
	 * @param fileEndings
	 * @return
	 */
	private static boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}

	

	/**
	 * 根据不同的后缀imageView设置不同的值
	 * 
	 * @param fileName
	 */
	public static void setImage(Context context, String fileName,
			ImageView imageView) {
		if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingImage))) {
			imageView.setImageResource(R.drawable.file_icon_picture);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingWebText))) {
			imageView.setImageResource(R.drawable.file_icon_txt);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingPackage))) {
			imageView.setImageResource(R.drawable.file_icon_rar);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingAudio))) {
			imageView.setImageResource(R.drawable.file_icon_mp3);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingVideo))) {
			imageView.setImageResource(R.drawable.file_icon_video);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingText))) {
			imageView.setImageResource(R.drawable.file_icon_txt);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingPdf))) {
			imageView.setImageResource(R.drawable.file_icon_pdf);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingWord))) {
			imageView.setImageResource(R.drawable.file_icon_office);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingExcel))) {
			imageView.setImageResource(R.drawable.file_icon_office);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingPPT))) {
			imageView.setImageResource(R.drawable.file_icon_office);
		} else {
			imageView.setImageResource(R.drawable.file);
		}
	}
	/**
	 * 根据不同的后缀打开不同的文件
	 * 
	 * @param fileName
	 */
	public static void openFile(Context context, String fileName, File file) {
		Intent intent;
		if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingImage))) {
			intent = OpenFiles.getImageFileIntent(file);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingWebText))) {
			intent = OpenFiles.getHtmlFileIntent(file);
			context.startActivity(intent);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingPackage))) {
			intent = OpenFiles.getApkFileIntent(file);
			context.startActivity(intent);

		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingAudio))) {
			intent = OpenFiles.getAudioFileIntent(file);
			context.startActivity(intent);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingVideo))) {
			intent = OpenFiles.getVideoFileIntent(file);
			context.startActivity(intent);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingText))) {
			intent = OpenFiles.getTextFileIntent(file);
			context.startActivity(intent);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingPdf))) {
			intent = OpenFiles.getPdfFileIntent(file);
			context.startActivity(intent);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingWord))) {
			intent = OpenFiles.getWordFileIntent(file);
			context.startActivity(intent);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingExcel))) {
			intent = OpenFiles.getExcelFileIntent(file);
			context.startActivity(intent);
		} else if (checkEndsWithInStringArray(fileName, context.getResources()
				.getStringArray(R.array.fileEndingPPT))) {
			intent = OpenFiles.getPPTFileIntent(file);
			context.startActivity(intent);
		} else {
			Toast.makeText(context, R.string.open_file_error, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 
	 * 返回本地文件列表
	 * 
	 * @param 本地文件夹路径
	 * 
	 */
	public static List<File> getFileListByPath(String path) {

		File dir = new File(path);

		List<File> folderList = new ArrayList<File>();

		List<File> fileList = new ArrayList<File>();

		// 获取指定盘符下的所有文件列表。（listFiles可以获得指定路径下的所有文件，以数组方式返回）
		File[] files = dir.listFiles();
		// 如果该目录下面为空，则该目录的此方法执行
		if (files == null) {
			return folderList;
		}// 通过循环将所遍历所有文件
		for (int i = 0; i < files.length; i++) {

			if (!files[i].isHidden()) {

				if (files[i].isDirectory()) {
					folderList.add(files[i]);
				}
				if (files[i].isFile()) {
					fileList.add(files[i]);
				}
			}
		}
		folderList.addAll(fileList);

		return folderList;
	}
}
