// to compile C:\JobTests\Armour>java -cp ../ Armour.runIndex
// to execute C:\JobTests\Armour>javac -cp ../ runIndex.java

//to execute from ie - http://localhost:8080/login

package Armour;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;
import java.time.*;
import java.awt.Desktop;

import org.w3c.dom.*;

import javax.xml.parsers.*;
import org.xml.sax.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//import javax.ws.rs.ApplicationPath;
//import javax.ws.rs.core.Application;

public class runIndex {

	String pwd = "";
	String name = "";
	String userid = "";

    //============================================================================================
    public String getFileName(String xmlStr, String xmlN){

	System.out.println("gf - start - "+xmlStr);

	try
	{
	   	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	      	DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	    	Document doc = docBuilder.parse (new InputSource( new StringReader(xmlStr)));

	   	// normalize text representation
	        doc.getDocumentElement ().normalize ();
		NodeList dList = doc.getElementsByTagName("filename");
		Element dElement = (Element)dList.item(0);
		NodeList textDList = dElement.getChildNodes();

		String xmlName = ((Node)textDList.item(0)).getNodeValue().trim();
		if (xmlName.equals("Input Screen")){
			xmlName = xmlN;
		}
		return xmlName;

        }catch (SAXParseException err) {
        	System.out.println ("gf - ** Parsing error" + ", line " 
			+ err.getLineNumber () + ", uri " + err.getSystemId ()+" " + err.getMessage ());
		return "gf - parsing error " + err.getMessage ();

        }catch (ParserConfigurationException pce) {
        	System.out.println ("gf - ** Parsing error" + " " + pce.getMessage ());
		return "gf error " + pce.getMessage ();

        }catch (SAXException e) {
        	System.out.println ("gf - saxe - " + e.getMessage ());
		return "gf - saxe error " + e.getMessage ();

	}catch (IOException ioe) {
        	System.out.println ("gf - ioe - " + ioe.getMessage ());
		return "IO error gf - " + ioe.getMessage ();

 	}catch (Exception exc) {
        	System.out.println ("gf - exc - " + exc.getMessage ());
		return "gf - excep - error " + exc.getMessage ();
	}
    }

    //============================================================================================
    public String loadForm(String fileName, String hflag, String xml, String xmlName){
	try
	{
		String xx = "";
		boolean inputForm = false;

		fileName = fileName + ".html";

		System.out.println("loading form "+fileName);

		String str = "";
		int siz = 0;
		boolean remLine = false;
		RandomAccessFile bf = new RandomAccessFile(fileName, "r");
		while ((xx=bf.readLine())!=null){
			str += xx+"\n";
			siz++;
		}
		bf.close();
		if (siz==0){
			str="99Form not loaded";
			System.out.println("loadform - "+str);
		}
		return str;
	}
        catch (FileNotFoundException f)
        {
            	System.out.println("loadform - fnf - "+f.getMessage());
		return "99form not found "+fileName;
	}
        catch (IOException io)
        {
            	System.out.println("loadform - ioe - "+io.getMessage());
		return "99ioexcep "+io.getMessage()+" "+fileName;
	}
        catch (Exception e)
        {
            	System.out.println("loadform - excep - "+e.getMessage());
		return "99excep "+e.getMessage()+" "+fileName;
	}
    }

    //======================================================================
    public String readXML(String fileName){
	try
	{
		String xx = "";
		fileName = getPath("xml") + fileName + ".xml";

		System.out.println("loading xml "+fileName);

		String str = "";
		int siz = 0;
		RandomAccessFile bf = new RandomAccessFile(fileName, "r");
		while ((xx=bf.readLine())!=null){
			str += xx+"\n";
			siz++;
		}
		bf.close();
		if (siz==0){
			str="99XML not loaded";
			System.out.println("readxml - "+str);
		}
		return str;
	}
        catch (FileNotFoundException f)
        {
            	System.out.println("readxml - fnf - "+f.getMessage());
		return "99xml not found "+fileName;
	}
        catch (IOException io)
        {
            	System.out.println("readXML - ioe - "+io.getMessage());
		return "99ioexcep "+io.getMessage()+" "+fileName;
	}
        catch (Exception e)
        {
            	System.out.println("readXML - excep - "+e.getMessage());
		return "99excep "+e.getMessage()+" "+fileName;
	}
    }
    //=============================================================
    public String processUser(String msg){

	try
	{
		System.out.println("pm - msg="+msg+" "+msg.indexOf("name="));
		String id = msg.substring(msg.indexOf("name=")+5,msg.indexOf("?pass"));
		String pw = msg.substring(msg.indexOf("password=")+9,msg.indexOf(" HTTP"));
		System.out.println("pm - id="+id+" pw="+pw);

		if (!id.equals(userid)) return "Invalid User";
		if (!pw.equals(pwd)) return "Invalid User";

		return "Hello "+name;

	} catch (StringIndexOutOfBoundsException se) {
	    	System.out.println("OutOfBounds caught when processingUser " + se.getMessage());
		return "Error Processing user";

	}

    }
    //=============================================================
    public String processMessage(String msg){
	//process message from ie, if first one rgen will have start or index in the line

	String str = "";
	if (msg.indexOf("/login")>-1){
		str = loadForm("login","no","","");

	}else if (msg.indexOf("password=")>-1){
		str = processUser(msg);
	}else{
		str = "Message not processed "+msg;
	}

	return str;
    }

    //=========================================================================
    public boolean getMessage(int portNumber){
    
	System.out.println("Starting getMessage for port "+portNumber);

      	try (
		ServerSocket serverSocket = new ServerSocket(portNumber);
	        Socket clientSocket = serverSocket.accept();
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    )
 	{

	  	System.out.println("Client connected on port " + portNumber +". Servicing requests.");
	  	String inputLine = "";
	  	while ((inputLine = in.readLine()) != null) {
            		System.out.println("Received message: <" + inputLine+">");
			if (inputLine.indexOf("POST")>-1 || inputLine.indexOf("GET")>-1){
				break;
			}
	  	}
		System.out.println(" from " + clientSocket.toString());
		String str = "";

  		// then create and start the thread, which is a
  		// subclass of Thread that can take a result setter:
  		ThreadWithResult th = new ThreadWithResult(inputLine);
  		th.start();	

		Thread.sleep(500);		//initially sleep 1/2sec to allow thread to complete.

		int i = 0;
		while (th.isAlive()){
			Thread.sleep(1000);	//slow request processing so sleep for 1sec, every 4sec flush ie
			i++;
			if (i > 3){
				//System.out.println("flushing out buffer i="+i);
				out.flush();
				i = 0;
			}
		}
		str = th.getStr();
	
		System.out.println("cthread="+Thread.currentThread().getName()+" threadname "+th.getName()+
					" isalive="+th.isAlive());

		//if (inputLine.indexOf("index")>-1){
			out.write(	"HTTP/1.1 200 OK\r\n"+
        				"Content-Type: text/html; application/javascript; charset=utf-8\r\n"+
        				"Content-Length: "+str.length()+"\r\n\r\n" +
        				str+"\n");
		//}else{
			//out.write(str);
		//}

		System.out.println("sent return message len="+str.length());

		out.flush();
   		out.close();
    		in.close();
    		clientSocket.close();
    		serverSocket.close();
		return true;

	} catch (IOException e) {
	    	System.out.println("IOException caught when trying to listen on port "
		    + portNumber + " " + e.getMessage());
		return true;
       	} catch (Exception ex) {
	    	System.out.println("Exception caught when trying to listen on port "
		    + portNumber + " " + ex.getMessage());
		return true;
	}
    }

	//===================================================================================
	public String getPath(String pathType)
	{

		try
		{
           		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            		Document doc = docBuilder.parse (new File("davefish1.xml"));

            		// normalize text representation
            		doc.getDocumentElement ().normalize ();

			String returnPath = System.getProperty("user.dir") + "/";

			if (pathType.equals("xml")){
				NodeList xmlList = doc.getElementsByTagName("xmlpath");
            			Element xmlElement = (Element)xmlList.item(0);
				NodeList textXMLList = xmlElement.getChildNodes();
				returnPath += ((Node)textXMLList.item(0)).getNodeValue().trim() + "/";

			}else if(pathType.equals("html")){
				NodeList htmlList = doc.getElementsByTagName("htmlpath");
            			Element htmlElement = (Element)htmlList.item(0);
				NodeList textHTMLList = htmlElement.getChildNodes();
				returnPath += ((Node)textHTMLList.item(0)).getNodeValue().trim() + "/";

			}else if(pathType.equals("data")){
				NodeList dataList = doc.getElementsByTagName("datapath");
            			Element dataElement = (Element)dataList.item(0);
				NodeList textDATAList = dataElement.getChildNodes();
				returnPath += ((Node)textDATAList.item(0)).getNodeValue().trim() + "/";

			}else if(pathType.equals("tmp")){
				NodeList dataList = doc.getElementsByTagName("tmppath");
            			Element dataElement = (Element)dataList.item(0);
				NodeList textDATAList = dataElement.getChildNodes();
				returnPath += ((Node)textDATAList.item(0)).getNodeValue().trim() + "/";

			}else if(pathType.equals("name")){
				NodeList dataList = doc.getElementsByTagName("name");
            			Element dataElement = (Element)dataList.item(0);
				NodeList textDATAList = dataElement.getChildNodes();
				returnPath = ((Node)textDATAList.item(0)).getNodeValue().trim();

			}else if(pathType.equals("username")){
				NodeList dataList = doc.getElementsByTagName("username");
            			Element dataElement = (Element)dataList.item(0);
				NodeList textDATAList = dataElement.getChildNodes();
				returnPath = ((Node)textDATAList.item(0)).getNodeValue().trim();

			}else if(pathType.equals("password")){
				NodeList dataList = doc.getElementsByTagName("password");
            			Element dataElement = (Element)dataList.item(0);
				NodeList textDATAList = dataElement.getChildNodes();
				returnPath = ((Node)textDATAList.item(0)).getNodeValue().trim();

			}else{
				returnPath = pathType + " path not found";
			}

			//System.out.println("gatPath - retPath="+returnPath);
			return returnPath;

        	}catch (SAXParseException err) {
        		System.out.println ("** Parsing error" + ", line " 
				+ err.getLineNumber () + ", uri " + err.getSystemId ()+" " + err.getMessage ());
			return "parsing error " + err.getMessage ();

        	}catch (ParserConfigurationException pce) {
        		System.out.println ("** Parsing error" + " " + pce.getMessage ());
			return "pce error " + pce.getMessage ();

        	}catch (SAXException e) {
        		System.out.println ("gp - saxe - " + e.getMessage ());
			return "saxe error " + e.getMessage ();

		}catch (IOException ioe) {
        		System.out.println ("gp - ioe - " + ioe.getMessage ());
			return "IO error " + ioe.getMessage ();
 		}catch (Exception exc) {
        		System.out.println ("gp - exc - " + exc.getMessage ());
			return "gp - excep - error " + exc.getMessage ();
		}

 	}

	//===================================================================================
	static public void main(String[] args)
	{
 		//System.out.println("Usage: java runIndex b4 html");
		//runIndex.displayHTML(xx);
 		System.out.println("Usage: java runIndex post html - path - "+System.getProperty("user.dir"));

		runIndex ri = new runIndex();

		ri.pwd = ri.getPath("password");
		ri.name = ri.getPath("name");
		ri.userid = ri.getPath("username");
 		System.out.println("name="+ri.name+" uid="+ri.userid+" pwd="+ri.pwd);
		
		boolean loopx = true;
		while (loopx){
			loopx = ri.getMessage(8080);
		}
    	}

	//==================================================================
	public class ThreadWithResult extends Thread {

		String xx = "";
    		String str = "";
		public ThreadWithResult(String s){
			this.xx = s;
		}

		public String getStr(){
			return this.str;
		}

  		public void run() {
         		try {
			    	System.out.println("in threadwithresult "+xx);
				str = processMessage(xx);
               		} catch (Exception e) {
                	    	e.printStackTrace();
                	}
 		}
	}
}
