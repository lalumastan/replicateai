package icsdiscover.replicateai.model;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class FormBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String gender;
	
	private MultipartFile father, mother;

	public MultipartFile getFather() {
		return father;
	}

	public void setFather(MultipartFile father) {
		this.father = father;
	}

	public MultipartFile getMother() {
		return mother;
	}

	public void setMother(MultipartFile mother) {
		this.mother = mother;
	}	
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "FormBean [gender=" + gender + ", father=" + father + ", mother=" + mother + "]";
	}
}
