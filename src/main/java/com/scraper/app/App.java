package com.scraper.app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {


	//The url of the website. This is just an example
	private static final String webSiteURL = "http://defence.pk/threads/ah-1z-viper-1000-hellfire-ii-missiles-approved-for-sale-to-pakistan.369316/page-2";

	//Temp folder to save images to
	public static final String folderPath = "\\temp-images\\";
	
	//The name of the generated word doc
	private static final String fileName = "createdDoc.docx";

	public static void main(String[] args) {

		try {

			//Connect to the website and get the html
			Document doc = Jsoup.connect(webSiteURL).userAgent("Mozilla").get();

			//Get all elements with img tag ,
			Elements images = doc.getElementsByTag("img");

			//Blank Document
			XWPFDocument document= new XWPFDocument(); 
			

			for (Element image : images) {

				//for each element get the src url
				String src = image.absUrl("src");
				String imageHeight = image.attr("height");
				String imageWidth = image.attr("width");
				
				System.out.println("Image Found!");
				System.out.println("src attribute is : " + src);
				
				getImages(src, document);
			}
			
			//Write the Document in file system
			FileOutputStream out = new FileOutputStream(new File(folderPath+fileName));
			document.write(out);
			out.close();
			System.out.println("document written successully");

		} catch (IOException ex) {
			System.err.println("There was an error");
			Logger.getLogger(DownloadImages.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void getImages(String src, XWPFDocument document) throws IOException, InvalidFormatException {

        String folder = null;

        //Exctract the name of the image from the src attribute
        int indexname = src.lastIndexOf("/");

        if (indexname == src.length()) {
            src = src.substring(1, indexname);
        }

        String name = src.substring(indexname, src.length());

        System.out.println(name);
        
        //Remove any query parameters
        if (name.lastIndexOf("?") > 0) {
        	name = name.substring(1, name.lastIndexOf("?"));
        }

        //Open a URL Stream
        URL url = new URL(src);
        
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.addRequestProperty("User-Agent", "Mozilla/4.0");
        
        InputStream in = httpCon.getInputStream();
        
//        XWPFParagraph title = document.createParagraph();    
//        XWPFRun run = title.createRun();
//        run.setText(name);
//        run.setBold(true);
//        title.setAlignment(ParagraphAlignment.CENTER);
//        
//        run.addBreak();
//        run.addPicture(in, XWPFDocument.PICTURE_TYPE_JPEG, name, 12,12);
        
        
//        OutputStream out = new BufferedOutputStream(new FileOutputStream(App.folderPath+ name));
//
//        for (int b; (b = in.read()) != -1;) {
//            out.write(b);
//        }
//        out.close();
        in.close();

    }
}

