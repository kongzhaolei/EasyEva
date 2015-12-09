package theEasyEva;

import java.sql.Connection;

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
	
	static EasyDB sourcedb;

	/**
	 * �������Զ����ƹ���
	 * 
	 */
	public static void main(String[] args) {
		// ����Display,��Ӧ����ϵͳ�Ŀؼ���ʹ��������ͷ�
		Display display = new Display();
		// ����shell
		final Shell shell = new Shell();
		shell.setSize(753, 502);
		shell.setText("EasyEva-���ٴ���Կ�");
		
		Group G_config = new Group(shell, SWT.NONE);
		G_config.setText("��������");
		G_config.setBounds(10, 10, 717, 132);

		Label L_databasetype = new Label(G_config, SWT.NONE);
		L_databasetype.setBounds(10, 26, 96, 17);
		L_databasetype.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		L_databasetype.setText("ѡ�����ݿ����ͣ�");

		C_databasetype = new Combo(G_config, SWT.NONE);
		C_databasetype.setBounds(110, 23, 154, 25);
		C_databasetype.setItems(str);

		Label L_sourceaddress = new Label(G_config, SWT.NONE);
		L_sourceaddress.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		L_sourceaddress.setBounds(10, 69, 107, 24);
		L_sourceaddress.setText("Դ�����ӵ�ַ��");

		Label L_targetaddress = new Label(G_config, SWT.NONE);
		L_targetaddress.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		L_targetaddress.setBounds(10, 99, 96, 17);
		L_targetaddress.setText("Ŀ������ӵ�ַ��");

		Label L_suserpass = new Label(G_config, SWT.NONE);
		L_suserpass.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		L_suserpass.setBounds(386, 69, 81, 17);
		L_suserpass.setText("�û���/���룺");

		Label L_tuserpass = new Label(G_config, SWT.NONE);
		L_tuserpass.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		L_tuserpass.setBounds(386, 99, 81, 17);
		L_tuserpass.setText("�û���/���룺");

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
		
		sourcedb = new EasyDB(T_sourceurl.getText(),
				T_sourceinstance.getText(), T_sourceuser.getText(),
				T_sourcepassword.getText());

		// ����Դ�����Ӱ�ť�¼�
		B_sourcetestconn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String farmquery = "SELECT wfname FROM CONFIG.wfinfo";
				MessageBox msg = new MessageBox(shell, SWT.ICON_INFORMATION);
				msg.setText("������ʾ");
				try {
					Connection source_conn = sourcedb.getConn(C_databasetype.getText());
					if (source_conn != null && !source_conn.isClosed()) {
						String[] str = sourcedb.Query(farmquery,"wfname");
						// ��������ӵ糡�б�
						C_selectfarm.setItems(str);
						msg.setMessage("Դ�����ӳɹ���well done��");
						msg.open();
					} else {
						msg.setMessage("Դ������ʧ�ܣ�try again��");
						msg.open();
					}
				} catch (Exception e) {
					msg.setMessage("Դ������ʧ�ܣ�try again��");
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

		// ����Ŀ������Ӱ�ť�¼�
		B_targettestconn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				EasyDB targetdb = new EasyDB(T_targeturl.getText(),
						T_targetinstance.getText(), T_targetuser.getText(),
						T_targetpassword.getText());
				MessageBox msg = new MessageBox(shell, SWT.ICON_INFORMATION);
				msg.setText("������ʾ");
				try {
					Connection target_conn = targetdb.getConn(C_databasetype.getText());
					if (target_conn != null && !target_conn.isClosed()) {
						msg.setMessage("Ŀ������ӳɹ���well done��");
						msg.open();
					} else {
						msg.setMessage("Ŀ�������ʧ�ܣ�try again��");
						msg.open();
					}
				} catch (Exception e) {
					msg.setMessage("Ŀ�������ʧ�ܣ�try again��");
					msg.open();
					e.printStackTrace();
				}
			}
		});

		Group G_source = new Group(shell, SWT.NONE);
		G_source.setBounds(10, 148, 254, 306);
		G_source.setText("Դ��");

		Label lblNewLabel = new Label(G_source, SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		lblNewLabel.setBounds(10, 27, 61, 17);
		lblNewLabel.setText("ѡ��糡��");

	    L_sourcetable = new List(G_source, SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL | SWT.READ_ONLY | SWT.SINGLE);
		L_sourcetable.setBounds(10, 58, 234, 238);

		C_selectfarm = new Combo(G_source, SWT.NONE);
		C_selectfarm.setBounds(70, 24, 174, 25);

		C_selectfarm.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				//��ѯ��configģʽ�����еı�
				String configquery = "select name from sys.tables where schema_id=(select schema_id from sys.schemas where name='config')";
				String[] str = sourcedb.Query(configquery,"name");
				L_sourcetable.setItems(str);
			}
		});

		Group G_target = new Group(shell, SWT.NONE);
		G_target.setBounds(318, 148, 409, 306);
		G_target.setText("Ŀ���");

		Label lblNewLabel_1 = new Label(G_target, SWT.NONE);
		lblNewLabel_1.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		lblNewLabel_1.setBounds(10, 27, 148, 17);
		lblNewLabel_1.setText("�������ñ��������ݣ�");

		L_targettable = new List(G_target, SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		L_targettable.setBounds(10, 57, 234, 239);

		Button B_importtable = new Button(G_target, SWT.NONE);
		B_importtable.setBounds(297, 105, 80, 27);
		B_importtable.setText("��ʼͬ��");

		Button B_addtable = new Button(G_target, SWT.NONE);
		B_addtable.setBounds(297, 172, 80, 27);
		B_addtable.setText("ѡ��ȫ��");

		Button B_deletetable = new Button(G_target, SWT.NONE);
		B_deletetable.setBounds(297, 243, 80, 27);
		// B_deletetable.setAlignment(SWT.LEFT);
		B_deletetable.setText("�������");
		// �������
		B_deletetable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {

			}
		});
		// ѡ��ȫ��
		B_addtable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
                
			}
		});
		// ��ʼͬ�����ݿ�
		B_importtable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// ��ѯ��ǰ�糡
				String farm = C_selectfarm.getText();
			}
		});
		
		//L_sourcetable˫��ѡ�����ñ�
		L_sourcetable.addListener(SWT.MouseDoubleClick,new Listener(){
			@Override
			public void handleEvent(Event event) {
				//��ȡ��ǰѡ���������ֵ
				int index  = L_sourcetable.getSelectionIndex();
				//�ж��Ƿ��ѡ����˫��
				if(index <= -1){
					return;
				}
				//��ȡѡ�����ֵ
				String configitem = L_sourcetable.getItem(index);
				L_targettable.add(configitem);
				L_sourcetable.remove(configitem);
			}
		});
		
		//L_targettable˫��ɾ�����ñ�
		L_targettable.addListener(SWT.MouseDoubleClick,new Listener(){
			@Override
			public void handleEvent(Event event) {
				//��ȡ��ǰѡ���������ֵ
				int index  = L_targettable.getSelectionIndex();
				if(index <= -1){
					return;
				}
				//��ȡѡ�����ֵ
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