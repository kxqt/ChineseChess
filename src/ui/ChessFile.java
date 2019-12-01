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

	// ���ַ����浽ָ�����͵��ļ���
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
				if (!file.exists()) {// �ļ������� �򴴽�һ��
					file.createNewFile();
				}
				fos = new FileOutputStream(file);
				fos.write(string.getBytes());
				fos.flush();
			} catch (IOException e) {
				System.err.println("�ļ�����ʧ�ܣ�");
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

	// ��ȡtxt�ļ��е��ַ���
	public static String[] readFile() {
		return readFile(".txt");
	}

	//��ȡָ�������ļ��е��ַ���
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
