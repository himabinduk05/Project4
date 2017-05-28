package Methods;

import javax.xml.bind.annotation.XmlElement;
public class DateOutput {
	private String date;
	 @XmlElement(name="DATE")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public DateOutput(String date) {
		super();
		this.date = date;
	}
	public DateOutput (){
		 
	 }
}
