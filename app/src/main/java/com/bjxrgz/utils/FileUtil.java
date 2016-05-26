package com.bjxrgz.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.bjxrgz.startup.MyApp;

import android.util.Log;

/**
 * Created by fd.meng on 2014/03/30
 *
 * 文件处理类
 *
 */
public class FileUtil {

	/**
	 * 记录日志
	 *
	 * @param printLog
	 * @param content
	 * @param isEncrypt
     */
	public static void writeLogFile(boolean printLog, String content, boolean isEncrypt) {
		if (content == null || content.trim().equals("")) {
			return;
		}

		if (printLog) {
			writeFile(MyApp.getLogFile(), content, isEncrypt);
		}
	}

	/**
	 * 写文件
	 *
	 * @param name
	 * @param content
	 * @param isEncrypt
     */
	private static void writeFile(String name, String content, boolean isEncrypt) {

		OutputStreamWriter osw = null;
		try {
			File file = new File(name);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			} else if (file.length() > 1024 * 60) {// 60k
				file.delete();
				file.createNewFile();
			}
			osw = new OutputStreamWriter(new FileOutputStream(file, true));
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss|SSS");
			String time = "---" + (sdf.format(new Date()));
			String logStr = content + time;
			if (isEncrypt) {
				logStr = Base64.encode(logStr.getBytes("utf-8"));
			}
			osw.write(logStr + "\n");
		} catch (Exception ex) {
			Log.e(MyApp.LOG_TAG, ((ex.getMessage() == null) ? "" : ex.getMessage()));
		} finally {
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					Log.e(MyApp.LOG_TAG, ((e.getMessage() == null) ? "" : e.getMessage()));
				}
			}
		}
	}

	/**
	 * 写日志
	 * @param printLog
	 * @param e
     */
	public static void writeLogFile(boolean printLog, Exception e) {
		if (printLog) {
			try {
				e.printStackTrace(new PrintStream(MyApp.getLogFile()));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception ex) {
				Log.e(MyApp.LOG_TAG, ((ex.getMessage() == null) ? "" : ex.getMessage()));
			}
		}
	}

	/**
	 * 读 文本文件
	 * @param fileName
	 * @return
	 * @throws Exception
     */
	public static String readTxtFile(File fileName) throws Exception {
		String result = "";
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			try {
				String read = null;
				while ((read = bufferedReader.readLine()) != null) {
					result = result + read;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		}
		//System.out.println("读取出来的文件内容是�?" + "\r\n" + result);
		return result;
	}

	/**
	 * get file's CRC32 hash code
	 * 
	 * @param fileName
	 * @return
	 */
	public static String hashFile(String fileName) {
		File f = new File(fileName);
		f.hashCode();
		try { // 对文件进行crc校验
				// long begin = System.currentTimeMillis();
				// FileInputStream in = new
				// FileInputStream("e:\\developer_tools\\SnowOSX3.6.iso");//
				// 指定目标文件
			FileInputStream in = new FileInputStream(fileName);// 指定目标文件
			FileChannel channel = in.getChannel(); // 从文件中获取�?个�?�道
			CRC32 crc = new CRC32();
			int length = (int) channel.size();
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, length); // 用只读模式从该�?�道获取字节缓冲，实现文件到内存的映�?
			for (int i = 0; i < length; i++) {
				int c = buffer.get(i);
				crc.update(c);// 按字节做crc
			}

			return (Long.toHexString(crc.getValue())).toUpperCase();

			// /System.out.println("crc code�?" +
			// (Long.toHexString(crc.getValue())).toUpperCase());
			// long end = System.currentTimeMillis();
			// System.out.println("运行" + (end - begin)/1000 + "s");
		} catch (Exception e) {
			Log.e(MyApp.LOG_TAG, "FileUtil->hashFile:" + e.toString());
		}
		return "";
	}

	/**
	 * 解压
	 *
	 * @param zipFile
	 * @param outPath
     */
	public static void unZip(String zipFile, String outPath) {
		InputStream is;
		ZipInputStream zis;
		try {
			// InputStream from File.
			// is = new FileInputStream(FILEPATHIN);

			// InputStream from URL
			is = new FileInputStream(zipFile);
			zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry zipEntry;

			// INTERNAL STORAGE
			// baseFolder = new File(c.getFilesDir(),
			// Constants.FOLDER_ZIP_OUTPUT);

			// EXTERNAL STORAGE
			File baseFolder = new File(outPath);
			if (!baseFolder.exists() && !baseFolder.isDirectory()) {
				baseFolder.mkdirs();
			}

			while ((zipEntry = zis.getNextEntry()) != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int count;

				String zipEntryName = zipEntry.getName();

				if (zipEntry.isDirectory()) {
					// FOLDER
					File zipEntryFolder = new File(outPath + File.separator + zipEntryName);
					if (!zipEntryFolder.isDirectory()) {
						zipEntryFolder.mkdirs();
					}
				} else {
					// FILE
					FileOutputStream fout = new FileOutputStream(outPath + File.separator + zipEntryName);
					// reading and writing
					while ((count = zis.read(buffer)) != -1) {
						baos.write(buffer, 0, count);
						byte[] bytes = baos.toByteArray();
						fout.write(bytes);
						baos.reset();
					}

					fout.close();
					zis.closeEntry();
				}
			}

			zis.close();
		} catch (IOException e) {
			// Log.e(this.getClass().getSimpleName(), "IOException", e);
			// cancel(true);
		} catch (Exception e) {
			// Log.e(this.getClass().getSimpleName(), "Exception", e);
			// cancel(true);
		}
	}

	/**
	 * 删除文件
	 * @param fileName
	 * @return
     */
	public static boolean delFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile()) {
			if (!file.exists()) {
				return true;
			}
			return file.delete();
		} else if (file.isDirectory()) {
			boolean failed = false;

			if (file.listFiles() != null) {
				for (File f : file.listFiles()) {
					if (!delFile(f.getPath())) {
						failed = true;
						break;
					}
				}
			}
			if (!failed) {
				if (!file.exists()) {
					return true;
				}
				return file.delete();
			}
		}

		return false;
	}

}
