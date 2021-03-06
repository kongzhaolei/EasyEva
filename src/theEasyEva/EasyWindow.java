package theEasyEva;

import java.sql.Connection;
import java.util.Arrays;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

public class EasyWindow {

	static Text T_sourceurl;
	static Text T_sourceinstance;
	static Text T_sourceuser;
	static Text T_sourcepassword;

	static Text T_targeturl;
	static Text T_targetinstance;
	static Text T_targetuser;
	static Text T_targetpassword;

	static Combo C_databasetype;
	static Combo C_selectfarm;
	static String str[] = { "sqlserver", "postgres", "mysql" };
	
	static List L_sourcetable;
	static List L_targettable;
	static String defaulttables[] = {"wfinfo","wfsystemconfig","groupinfo","lineinfo","plcinfo",
		"portinfo","serviceinfo","proxypcinfo","guarpow","wttypeinfo","wtinfo","linedevice"};  //默认的12张配置表
	
	static EasyDB sourcedb;

	/**
	 * 基础库自动复制工具
	 * 
	 */
	public static void main(String[] args) {
		// 创建Display,对应操作系统的控件，使用完必须释放
		Display display = new Display();
		// 创建shell
		final Shell shell = new Shell();
		shell.setSize(753, 502);
		shell.setText("EasyEva-快速搭建测试库");
		
		Group G_config = new Group(shell, SWT.NONE);
		G_config.setText("配置连接");
		G_config.setBounds(10, 10, 717, 132);

		Label L_databasetype = new Label(G_config, SWT.NONE);
		L_databasetype.setBounds(10, 26, 96, 17);
		L_databasetype.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		L_databasetype.setText("选择数据库类型：");

		C_databasetype = new Combo(G_config, SWT.NONE);
		C_databasetype.setBounds(110, 23, 154, 25);
		C_databasetype.setItems(str);

		Label L_sourceaddress = new Label(G_config, SWT.NONE);
		L_sourceaddress.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		L_sourceaddress.setBounds(10, 69, 107, 24);
		L_sourceaddress.setText("源库连接地址：");

		Label L_targetaddress = new Label(G_config, SWT.NONE);
		L_targetaddress.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		L_targetaddress.setBounds(10, 99, 96, 17);
		L_targetaddress.setText("目标库连接地址：");

		Label L_suserpass = new Label(G_config, SWT.NONE);
		L_suserpass.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		L_suserpass.setBounds(386, 69, 81, 17);
		L_suserpass.setText("用户名/密码：");

		Label L_tuserpass = new Label(G_config, SWT.NONE);
		L_tuserpass.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		L_tuserpass.setBounds(386, 99, 81, 17);
		L_tuserpass.setText("用户名/密码：");

		T_sourceurl = new Text(G_config, SWT.BORDER | SWT.READ_ONLY);
		T_sourceurl.setBounds(111, 66, 153, 23);
		T_sourceurl.setText("10.10.0.1");

		T_sourceinstance = new Text(G_config, SWT.BORDER | SWT.READ_ONLY);
		T_sourceinstance.setBounds(270, 66, 96, 23);
		T_sourceinstance.setText("scadadb");

		T_sourceuser = new Text(G_config, SWT.BORDER | SWT.READ_ONLY);
		T_sourceuser.setBounds(473, 66, 73, 23);
		T_sourceuser.setText("sa");

		T_sourcepassword = new Text(G_config, SWT.BORDER | SWT.READ_ONLY);
		T_sourcepassword.setBounds(552, 66, 73, 23);
		T_sourcepassword.setText("dbadmin");	
		T_sourcepassword.setEchoChar('*');

		Button B_sourcetestconn = new Button(G_config, SWT.NONE);
		B_sourcetestconn.setBounds(640, 65, 64, 27);
		B_sourcetestconn.setText("Test");
		
		Group G_target = new Group(shell, SWT.NONE);
		G_target.setBounds(318, 148, 409, 306);
		G_target.setText("目标库");

		Label lblNewLabel_1 = new Label(G_target, SWT.NONE);
		lblNewLabel_1.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		lblNewLabel_1.setBounds(10, 27, 148, 17);
		lblNewLabel_1.setText("以下配置表将导入数据：");

		L_targettable = new List(G_target, SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		L_targettable.setBounds(10, 57, 234, 239);

		final Button B_importtable = new Button(G_target, SWT.NONE);
		B_importtable.setBounds(297, 105, 80, 27);
		B_importtable.setText("开始同步");
		B_importtable.setEnabled(false);

		final Button B_addtable = new Button(G_target, SWT.NONE);
		B_addtable.setBounds(297, 172, 80, 27);
		B_addtable.setText("智能选择");
		B_addtable.setEnabled(false);

		final Button B_deletetable = new Button(G_target, SWT.NONE);
		B_deletetable.setBounds(297, 243, 80, 27);
		// B_deletetable.setAlignment(SWT.LEFT);
		B_deletetable.setText("删除全部");
		B_deletetable.setEnabled(false);
		
		sourcedb = new EasyDB(T_sourceurl.getText(),
				T_sourceinstance.getText(), T_sourceuser.getText(),
				T_sourcepassword.getText());

		// 测试源库连接按钮事件
		B_sourcetestconn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String farmquery = "SELECT wfname FROM CONFIG.wfinfo";
				MessageBox msg = new MessageBox(shell, SWT.ICON_INFORMATION);
				msg.setText("连接提示");
				try {
					Connection source_conn = sourcedb.getConn(C_databasetype.getText());
					if (source_conn != null && !source_conn.isClosed()) {
						String[] str = sourcedb.Query(farmquery,"wfname");
						// 下拉框添加电场列表
						C_selectfarm.setItems(str);
						B_addtable.setEnabled(true);
						B_deletetable.setEnabled(true);
						msg.setMessage("源库连接成功，well done！");
						msg.open();
					} else {
						msg.setMessage("源库连接失败，try again！");
						msg.open();
					}
				} catch (Exception e) {
					msg.setMessage("源库连接失败，try again！");
					msg.open();
					e.printStackTrace();
				}
			}
		});

		T_targeturl = new Text(G_config, SWT.BORDER);
		T_targeturl.setBounds(112, 96, 153, 23);

		T_targetinstance = new Text(G_config, SWT.BORDER);
		T_targetinstance.setBounds(270, 96, 96, 23);

		T_targetuser = new Text(G_config, SWT.BORDER);
		T_targetuser.setBounds(473, 96, 73, 23);

		T_targetpassword = new Text(G_config, SWT.BORDER);
		T_targetpassword.setBounds(552, 96, 73, 23);
		T_sourcepassword.setEchoChar('*');

		Button B_targettestconn = new Button(G_config, SWT.NONE);
		B_targettestconn.setText("Test");
		B_targettestconn.setBounds(640, 95, 64, 27);

		// 测试目标库连接按钮事件
		B_targettestconn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				EasyDB targetdb = new EasyDB(T_targeturl.getText(),
						T_targetinstance.getText(), T_targetuser.getText(),
						T_targetpassword.getText());
				MessageBox msg = new MessageBox(shell, SWT.ICON_INFORMATION);
				msg.setText("连接提示");
				try {
					Connection target_conn = targetdb.getConn(C_databasetype.getText());
					if (target_conn != null && !target_conn.isClosed()) {
						msg.setMessage("目标库连接成功，well done！");
						msg.open();
						B_importtable.setEnabled(true);
					} else {
						msg.setMessage("目标库连接失败，try again！");
						msg.open();
					}
				} catch (Exception e) {
					msg.setMessage("目标库连接失败，try again！");
					msg.open();
					e.printStackTrace();
				}
			}
		});

		Group G_source = new Group(shell, SWT.NONE);
		G_source.setBounds(10, 148, 254, 306);
		G_source.setText("源库");

		Label lblNewLabel = new Label(G_source, SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		lblNewLabel.setBounds(10, 27, 61, 17);
		lblNewLabel.setText("选择电场：");

	    L_sourcetable = new List(G_source, SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL | SWT.READ_ONLY | SWT.SINGLE);
		L_sourcetable.setBounds(10, 58, 234, 238);

		C_selectfarm = new Combo(G_source, SWT.NONE);
		C_selectfarm.setBounds(70, 24, 174, 25);

		C_selectfarm.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				//查询出config模式下所有的表
				String configquery = "select name from sys.tables where schema_id=(select schema_id from sys.schemas where name='config')";
				String[] str = sourcedb.Query(configquery,"name");
				L_sourcetable.setItems(str);
			}
		});

		
		// 删除全部
		B_deletetable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				L_targettable.removeAll();
			}
		});
		// 智能选择
		B_addtable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				L_targettable.removeAll();
				for(int index=0; index < defaulttables.length; index++){
					if(Arrays.asList(L_sourcetable.getItems()).contains(defaulttables[index])){
						L_targettable.add(defaulttables[index]);
						L_sourcetable.remove(defaulttables[index]);
					}else{
						MessageBox msg = new MessageBox(shell, SWT.ICON_INFORMATION);
						msg.setText("智能选择");
						msg.setMessage("配置表: " + defaulttables[index] + "不存在");
						msg.open();
						continue;
					}
				}
				B_addtable.setEnabled(false);
			}
		});
		
		// 开始同步数据库
		B_importtable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// 查询当前电场
				String farm = C_selectfarm.getText();
				MessageBox msg = new MessageBox(shell, SWT.ICON_INFORMATION);
				msg.setText("开始同步");
				try{	
				//遍历L_targettable的每个表并执行同步
				for(int index= 0; index < L_targettable.getItemCount(); index++){
				String table = L_targettable.getItem(index);
				//对config模式中有wfid列的表进行数据行插入
				String baksql_wfid = "insert opendatasource('SQLOLEDB','Data Source= "+T_targeturl.getText()+";"
						+ "User ID="+T_targetuser.getText()+";Password="+T_targetpassword.getText()+"')"
						+ "."+T_targetinstance.getText()+".config."+table+" "
						+ "select * from config."+table+" where wfid = (select wfid from config.wfinfo where wfname = '"+farm+"')";
				
				//无wfid列，针对设备组管理groupinfo表的定制sql
				String baksql_parentid = "insert opendatasource('SQLOLEDB','Data Source= "+T_targeturl.getText()+";"
						+ "User ID="+T_targetuser.getText()+";Password="+T_targetpassword.getText()+"')"
						+ "."+T_targetinstance.getText()+".config."+table+" "
						+ "select * from config."+table+" where parentid = (select cast(wfid as varchar(50)) from config.wfinfo where wfname = '"+farm+"')";
				
				//无wfid列，针对担保曲线guarpow表的定制sql
				String baksql_id = "insert opendatasource('SQLOLEDB','Data Source= "+T_targeturl.getText()+";"
						+ "User ID="+T_targetuser.getText()+";Password="+T_targetpassword.getText()+"')"
						+ "."+T_targetinstance.getText()+".config."+table+" "
						+ "select * from config."+table+" where left(cast(id AS varchar(10)),6) = (select cast(wfid as varchar(50)) from config.wfinfo where wfname = '"+farm+"')";
				
				//无wfid列，针对设备类型表的定制sql
				String baksql_protocolid = "insert opendatasource('SQLOLEDB','Data Source= "+T_targeturl.getText()+";"
						+ "User ID="+T_targetuser.getText()+";Password="+T_targetpassword.getText()+"')"
						+ "."+T_targetinstance.getText()+".config."+table+" "
						+ "select * from config."+table+" wts where wts.protocolid not in (select wtt.protocolid from "+T_targetinstance.getText()+".config."+table+" wtt)";
				
				if(table.equals("groupinfo")){
					sourcedb.excutesql(baksql_parentid);
				    }
				else{
					if(table.equals("guarpow")){
					    sourcedb.excutesql(baksql_id);
					}
					if(table.equals("wttypeinfo")){
						sourcedb.excutesql(baksql_protocolid);
					}
					else{
				    sourcedb.excutesql(baksql_wfid);
					    }
				    }
				}
				    msg.setMessage("数据库同步完成！");
				    msg.open();
				}catch(Exception e){
					e.printStackTrace();
					msg.setMessage("数据库同步失败！");
					msg.open();
				}
			}
		});
		
		//L_sourcetable双击选择配置表
		L_sourcetable.addListener(SWT.MouseDoubleClick,new Listener(){
			@Override
			public void handleEvent(Event event) {
				//获取当前选中项的索引值
				int index  = L_sourcetable.getSelectionIndex();
				//判断是否对选中项双击
				if(index <= -1){
					return;
				}
				//获取选择项的值
				String configitem = L_sourcetable.getItem(index);
				L_targettable.add(configitem);
				L_sourcetable.remove(configitem);
			}
		});
		
		//L_targettable双击删除配置表
		L_targettable.addListener(SWT.MouseDoubleClick,new Listener(){
			@Override
			public void handleEvent(Event event) {
				//获取当前选中项的索引值
				int index  = L_targettable.getSelectionIndex();
				if(index <= -1){
					return;
				}
				//获取选择项的值
				String configitem = L_targettable.getItem(index);
				L_targettable.remove(configitem);
				L_sourcetable.add(configitem);   
			}
		});

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}