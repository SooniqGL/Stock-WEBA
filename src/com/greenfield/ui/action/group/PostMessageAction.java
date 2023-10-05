package com.greenfield.ui.action.group;

import java.io.File;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.greenfield.common.base.BaseAction;
import com.greenfield.common.object.group.MessageVO;
import com.greenfield.common.tool.FileTools;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.imagetool.ImageResizer;
import com.greenfield.common.nosql.object.message.DocumentStore;
import com.greenfield.ui.handler.group.MessageHandler;

public class PostMessageAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	/** page id string and mode */
	public static final String BLANK 	= "blank";
	public static final String UPDATE 	= "update";

	/** return string */
	public static final String MESSAGE_HOME 		= "messagehome";
	public static final String NEW_MESSAGE 			= "newmessage";

	/* size for the small images */
	private static final int IMAGE_SMALL_WIDTH 		= 300;
	private static final int IMAGE_SMALL_HEIGHT 	= 225;
	
	

	/* allow to load multiple files in one message */
	private File[] file;
	private String[] fileContentType;
	private String[] fileFileName;

	
	private MessageVO inputVO = new MessageVO();
	
	public String executeAction() throws Exception {

		String retString = MESSAGE_HOME;
		String mode = inputVO.getMode();
		String type = inputVO.getType();

		System.out.println("postmessage - mode: " + mode);
		System.out.println("type: " + inputVO.getType() + ", subject: " + inputVO.getMsgSubject());
		
		/*
		HttpServletRequest request = ServletActionContext.getRequest();
		Enumeration enParams = request.getParameterNames(); 
		while(enParams.hasMoreElements()){
		 String paramName = (String)enParams.nextElement();
		 System.out.println("Attribute Name - "+paramName+", Value - "+request.getParameter(paramName));
		}

		Enumeration enAttr = request.getAttributeNames(); 
		while(enAttr.hasMoreElements()){
		 String attributeName = (String)enAttr.nextElement();
		 System.out.println("Attribute Name - "+attributeName+", Value - "+(request.getAttribute(attributeName)).toString());
		} */



		// debug
		if (getFile() != null) {
			System.out.println("No of files = " + getFile().length);
			System.out.println("File Names are: " + Arrays.toString(getFileFileName()));
			System.out.println("File types are: " + Arrays.toString(getFileContentType()));
		}
		
		//System.out.println("Msg is:"+ msgText);
		
		try {
			if (mode == null || mode.equals("") || mode.equals("blank")) {
				mode = MESSAGE_HOME;
				inputVO.setMode(mode);
			}
			
			if (type == null || type.equals("")) {
					inputVO.setType(BLANK);
			}

			inputVO.setUserId(user.getUserId());
			inputVO.setNickname(user.getNickname());
			
			if (mode.equals(MESSAGE_HOME)) {
				// do not need to handle here, redirect it
				retString = mode;
			} else if (mode.equals(NEW_MESSAGE)) {
				
				if (type != null && type.equals(UPDATE)) {
					// subject/message/file are coming
					List<DocumentStore> fileList = collectFileList(inputVO.getToGroupList());
					inputVO.setFileList(fileList);

					MessageHandler handler = new MessageHandler();
					handler.execute(user, inputVO, sessionContext);
					
					// if it is submit, and success
					if (inputVO.getSuccess()) {
						retString = MESSAGE_HOME;
					} else {
						retString = NEW_MESSAGE;
					}
					
				} else {	
					MessageHandler handler = new MessageHandler();
					handler.execute(user, inputVO, sessionContext);
					retString = NEW_MESSAGE;
				}
				
			} 
		} catch (Exception e) {

			// c_logger.instr(ke);
			e.printStackTrace();
			inputVO.setMessage("Some system error: " + e.getMessage());
			inputVO.setSuccess(false);
			
			throw e;

		}

		return retString;
	}
	
	/**
	 * securityTag - list of togroup's, separated by common
	 * @param securityTag
	 * @return
	 * @throws Exception
	 */
	

	private List<DocumentStore> collectFileList(String securityTag) throws Exception {
		ArrayList<DocumentStore> fileList = new ArrayList<DocumentStore>();
		
		// check if the file is empty
		if (file == null || file.length == 0) {
			return fileList;
		}
		
		//Timestamp now = DateUtil.getTimestampAddSeconds(null, 0);
		
		for(int i=0; i < file.length; i++) {
			DocumentStore doc = new DocumentStore();
			
			doc.setDocName(fileFileName[i]);
			doc.setDocType(fileContentType[i]);

			byte[] fileData = FileTools.readFile(file[i]);
			byte[] imgSmall = null;
			
			// do the second image, only when we see images
			if (FileTools.isFileImage(fileContentType[i])) {
				imgSmall = ImageResizer.resize(file[i], fileContentType[i], IMAGE_SMALL_WIDTH, IMAGE_SMALL_HEIGHT);
				doc.setIsImage(true);
			} else {
				doc.setIsImage(false);
			}
			
			doc.setData1(ByteBuffer.wrap(fileData));	
			if (imgSmall != null) {
				doc.setData2(ByteBuffer.wrap(imgSmall));
			}
			
			// security tag - list of the togroup's
			doc.setSecurityTag(securityTag);
			// doc.setCreDate(now);  // do not set timestamp now, set it later using message's timestamp
			
			// only thing missing - doc id, will be created after message is created.
			fileList.add(doc);
			
			// debug
			//System.out.println("File Name is: " + file2FileName[i]);
			//System.out.println("File ContentType is: " + file2ContentType[i]);
			
		}
		
		return fileList;
	}

	
	public MessageVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(MessageVO inputVO) {
		this.inputVO = inputVO;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String[] getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String[] fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	
	/**
	 * For validation here, the customer will be directed to the "input" page
	 * which is new message page.
	 */
	public void validate() {
		if (getFile() != null) {
			if (getFile().length > 3) {
				addActionError("Maximum of 3 files accepted.");
			}

			/* file size controlled by the struts.xml - so do not need to do here
			for (int i = 0; i < getFile().length; i++) {
				System.out.println("======File size validation before upload: size in bytes: " + getFile()[i].length());
				if (getFile()[i].length() > 5200400) {
					// Give the size in bytes whatever you want to take
					addActionError("File is too large ! Select less than 5MB file");
				} else {
					//addActionMessage("File Uploaded successfully!");
				}
			} */
		}
	} 
	
	
}
