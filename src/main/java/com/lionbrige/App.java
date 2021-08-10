package com.lionbrige;

import java.io.File;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // UIFrame uiFrame = new UIFrame();
            // JFrame frame = new JFrame();
            // frame = new JFrame();
            // frame.setContentPane(uiFrame.getUiPanel());
            // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // frame.pack();
            // frame.setVisible(true);
            // uiFrame.initialize();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            return;
        }

        String folderPath = "";

        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // 디렉토리 설정
        chooser.setCurrentDirectory(new File(".")); // 현재 사용 디렉토리를 지정
        chooser.setAcceptAllFileFilterUsed(true); // Fileter 모든 파일 적용
        chooser.setDialogTitle("입력할 URL 엑셀파일"); // 창의 제목
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // 파일 선택 모드

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel file(2007~)", "xlsx"); // filter 확장자 추가
        chooser.setFileFilter(filter); // 파일 필터를 추가

        int returnVal = chooser.showOpenDialog(null); // 열기용 창 오픈

        if (returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭
            folderPath = chooser.getSelectedFile().toString();
        } else if (returnVal == JFileChooser.CANCEL_OPTION) { // 취소를 클릭
            System.out.println("cancel");
            folderPath = "";
        }

        File file = new File(folderPath);

        List<URLBundle> urlBundleList = XLSXReader.read(file);
        XLSXWriter writer = XLSXWriter.getInstance();
        int bundleCount = 1;
        int urlCount = 1;

        for (URLBundle urlBundle : urlBundleList) {
            writer.newSheet(urlBundle.getSheetName());
            List<String> urlList = urlBundle.getUrlList();
            for (String url : urlList) {
                Logger.titleLog(String.format("[%d/%d] - %d/%d", bundleCount,urlBundleList.size(),(urlCount++),urlList.size()));
                Logger.log(url);
                writer.add(MainProcess.run(url));
            }
            bundleCount++;
            Logger.log("saving...");
            writer.save();
        }

        
        Logger.titleLog("Done");
        Logger.log("done");
        writer.close();
        WebCrawler.getInstance().close();

        System.exit(0);
    }
}
