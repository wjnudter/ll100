package com.plistview;

public class Message_zy {
	private String remark;//作业评价
	private String date;//发布时间时间 published_on
	private String homeworkJson;//homework
	private String homeworkpaperJson;//homework_papers	
	private String classJason;//作业班级
	private String type;//作业类型
	private String msg;
	private String url;//homework_paper_endpoint
	private String contentUrl;//listen_text_endpoint
	private String id;//作业排序ID
    private Boolean ischeck;
	
	public Boolean getcheck() {
		return ischeck;
	}
	public void setcheck(Boolean a) {
		this.ischeck = a;
	}
	
	public String getremark() {
		return remark;
	}
	public void setremark(String a) {
		this.remark = a;
	}
	
	public String getid() {
		return id;
	}
	public void setid(String a) {
		this.id = a;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String a) {
		this.msg = a;
	}
	public String getdate() {
		return date;
	}
	public void setdate(String a) {
		this.date = a;
	}
	public String gethomeworkJson() {
		return homeworkJson;
	}
	public void sethomeworkJson(String a) {
		this.homeworkJson = a;
	}
	
	public String gethomeworkpaperJson() {
		return homeworkpaperJson;
	}
	public void sethomeworkpaperJson(String a) {
		this.homeworkpaperJson = a;
	}
	
	public String getclassJason() {
		return classJason;
	}
	public void setclassJason(String a) {
		this.classJason = a;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String a) {
		this.url = a;
	}
	public String getcontentUrl() {
		return contentUrl;
	}
	public void setcontentUrl(String a) {
		this.contentUrl = a;
	}
	public String gettype() {
		return type;
	}
	public void settype(String a) {
		this.type = a;
	}

}
