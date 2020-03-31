package onlineShop.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
// create authorities table
//这个表只用于检验用户
//和其他表格没有任何关联关系
@Entity
@Table(name = "authorities")
public class Authorities implements Serializable {
	//序列化和反序列化
	//数据库内也存这个随机号，用于校验
	//一般网上用一个随机数生成器产生数字
	private static final long serialVersionUID = 8734140534986494039L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	//用string来表示是普通用户还是admin
	private String emailId;

	private String authorities;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}
}
