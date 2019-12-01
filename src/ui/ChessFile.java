package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChessFile {

	// 将字符串存到指定类型的文件中
	public static void saveToFile(String string, String fileType) {
		FileNameExtensionFilter filter;
		if (fileType.equals(".log")) {
			filter = new FileNameExtensionFilter("*.log", "log");
		} else {
			filter = new FileNameExtensionFilter("*.txt", "txt");
		}
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(false);
		int result = fc.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if (!file.getPath().endsWith(fileType)) {
				file = new File(file.getPath() + fileType);
			}
			FileOutputStream fos = null;
			try {
				if (!file.exists()) {// 文件不存在 则创建一个
					file.createNewFile();
				}
				fos = new FileOutputStream(file);
				fos.write(string.getBytes());
				fos.flush();
			} catch (IOException e) {
				System.err.println("文件创建失败：");
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// 读取txt文件中的字符串
	public static String[] readFile() {
		return readFile(".txt");
	}

	//读取指定类型文件中的字符串
	public static String[] readFile(String fileType) {
		FileNameExtensionFilter filter;
		JFileChooser fc = new JFileChooser();
		ArrayList<String> strs = new ArrayList<String>();
		if (fileType.equals(".log")) {
			filter = new FileNameExtensionFilter("*.log", "log");
		} else {
			filter = new FileNameExtensionFilter("*.txt", "txt");
		}
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(false);
		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				BufferedReader bufr = new BufferedReader(new InputStreamReader(new FileInputStream(fc.getSelectedFile())));
				String line;
				while ((line = bufr.readLine()) != null) {
					strs.add(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return (String[]) strs.toArray(new String[strs.size()]);
	}

}
