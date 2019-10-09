package alan.User;

/**
 * 这是用户基本描述类(用户基本描述类包cn.itcast.pojo)
 *
 * @author 小华
 * @version V1.1
 */
public class User 
{
    //账户
    private String account;
    //密码
    private String password;
    //姓名
    private String name;

    public User() {}

    public User(String username, String password, String name) {
        this.account = username;
        this.password = password;
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return this.name;
    }
}
