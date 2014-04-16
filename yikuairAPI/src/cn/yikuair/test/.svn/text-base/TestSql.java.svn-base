package cn.yikuair.test;


public class TestSql {

	/**
	 * @param args
	 */
	
//	public List<User> getListData(){
//		List<User> list = new ArrayList<User>();
//		Connection conn = Sql.getConn();
//		try{
//			String sql = "select * from user";
//			PreparedStatement stmt = conn.prepareStatement(sql);
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()){
//				User user = new User();
//				user.setId(rs.getInt(1));
//				user.setUserName(rs.getString("username"));
//				user.setPwd(rs.getString("pwd"));
//				list.add(user);
//			}
//		}
//		catch(Exception ex){
//			ex.printStackTrace();
//		}finally{
//			try{
//				conn.close();
//			}
//			catch(Exception ex){
//				ex.printStackTrace();
//			}
//		}
//		return list;
//	}
//	
//	public boolean addData(User user){
//		String sql="";
//		if(user.id!=0){
//			sql = "insert into user values("+user.id+",'"+user.userName+"','"+user.pwd+"')";
//		} else {
//			sql = "insert into user(username,pwd) values('"+user.userName+"','"+user.pwd+"')";
//		}
//		Connection conn = Sql.getConn();
//		Statement stmt =  null;
//		try{
//			stmt =  conn.createStatement();
//			stmt.execute(sql);
//		} catch(Exception ex){
//			ex.printStackTrace();
//		} finally{
//			try{
//				stmt.close();
//				conn.close();
//			}
//			catch(Exception ex){
//				ex.printStackTrace();
//			}
//		}
//		return true;
//	}
//	
//	
//	public boolean saveData (String sql){
//		Connection conn = Sql.getConn();
//		Statement stmt =  null;
//		try{
//			stmt =  conn.createStatement();
//			boolean bool = stmt.execute(sql);
//			System.out.println("boolean:"+bool);
//		} catch(Exception ex){
//			ex.printStackTrace();
//		} finally{
//			try{
//				stmt.close();
//				conn.close();
//			}
//			catch(Exception ex){
//				ex.printStackTrace();
//			}
//		}
//		return true;
//	}
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 TestSql testSql = new TestSql();
//		List<User> list = testSql.getListData();
//		System.out.println(list.size());
//		String string = Utils.ListBeanToString(list);
//		System.out.println(string);
//		 User user = new User();
//		 user.setUserName("你真好");
//		 user.setPwd("123456");
//		testSql.addData(user);
		
		 String sql = "update user set username='刘德华' where id=1";//update
		 String sql2 = "delete from user where id=2";
//		 testSql.saveData(sql2);
		
	}
	
	

}
