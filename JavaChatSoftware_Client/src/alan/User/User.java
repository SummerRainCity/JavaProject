package alan.User;

public class User 
{
	private String account;
	private String name;
	
	public User() {}
	
	public User(String account, String name) {
		super();
		this.account = account;
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}