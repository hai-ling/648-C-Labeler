package labeler;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class Labeler {
	int index;
	int seat, max;
	int row = 1;
	int gym = 0;
	int RoomIndex;
	String capacity;
	String Output;
	List<String> Temp= new ArrayList<String>();
	int listSize = 0;
	List<String> Names= new ArrayList<String>();
	String Name[];
	String[] Code = new String[15];
	String[][][] Info = new String[5][5][5];
	int[] Capacity  = new int[3];
	PrintMsg Printdow = new PrintMsg();
	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Labeler window = new Labeler();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		readFile();
		loadData();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	public void loadData() {
		String temp;
		int i = 0;
		int j = 0;
		
		for (i = 0; i < listSize; i++) {
			temp = Temp.get(i);
			String[] tempArray, tempArray2,tempArray3;
			tempArray = temp.split("-");
			Names.add(tempArray[0]);
			Code[i] = tempArray[1];
			tempArray2 = tempArray[2].split(",");
			tempArray3 = tempArray[3].split(",");
			for (j = 0; j < tempArray2.length; j++) {
				Info[i][j][0] = tempArray2[j];
				Info[i][j][1] = tempArray3[j];	
			}
			
		}
		Name = Names.toArray(new String[Names.size()]);
		
	}
	
	public void readFile() {
        String fileName = "ExamVenues.txt";
        String line = null;
        
        try {
        	int i = 0;
            FileReader fileReader = 
                new FileReader(fileName);
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                Temp.add(line);
                System.out.println(Temp.get(i));
                i++;
                listSize++;
            }
            System.out.println(i);
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        }

	}// global labels and combo boxes
	String dateString;
	Combo Input, Input2;
	DateTime dateTime;
	Label lblBuilding, lblCode, lblRm, lblCapacity, lblDate, lblOutput;
	private TabItem tbtmDate;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Composite composite_1;
	private Button btnNext;
	private Text txtRow;
	private Label lblRm_1;
	private Text txtRm;
	
	public void refreshOptions() {
		String name, code, room;
		RoomIndex = Input2.getSelectionIndex();
		name = Name[index];
		code = Code[index];
		room = Info[index][RoomIndex][0];
		capacity = Info[index][RoomIndex][1];
		lblCapacity.setText(capacity);
		lblBuilding.setText(name);
		lblCode.setText(code);
		lblRm.setText(room);
		
		// date
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
		instance.set(Calendar.MONTH, dateTime.getMonth());
		instance.set(Calendar.YEAR, dateTime.getYear());
		dateString = new SimpleDateFormat("yyMMMdd").format(instance.getTime());
		dateString = dateString.toUpperCase();
		
		
		Output = code + " '" + dateString + " " + room;
		lblOutput.setText(Output + "-##");

	}
	public void PrintRm() {
		DecimalFormat x = new DecimalFormat("-##");
		PrinterService printerService = new PrinterService();
		System.out.println(printerService.getPrinters());
		String msg = "Record of Exam Attendance\nTHIS CARD IS KEPT ON FILE\n";
		try {
			row = Integer.parseInt(txtRow.getText());
		}
		catch(Exception F){
		}
		switch(gym) {
		case 0:
			if (max >= seat) {
				lblOutput.setText(Output + x.format(seat));
				System.out.println(Output + x.format(seat));
				printerService.printString("POS-58", msg + Output + x.format(seat)+"\n\n\n\n");
				txtRm.setText(seat+"");
				
			}
			break;
		case 1:
			if (max >= seat) {
				lblOutput.setText(Output + " " + row + x.format(seat));
				System.out.println(Output + " " + row + x.format(seat));
				printerService.printString("POS-58", msg + Output + " " + row + x.format(seat)+"\n\n\n\n");
				txtRm.setText(seat+"");
			}
			else if(max <= seat) {
				seat = 1; 
				row++;
				txtRow.setText(row+"");
				PrintRm();
				
			}
			break;
		}
		seat++;

		}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(245, 255, 250));
		shell.setSize(450, 276);
		shell.setText("SWT Application");
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(145, 0, 289, 179);
		
		Button btnTest = new Button(shell, SWT.FLAT);
		btnTest.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		btnTest.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.BOLD));
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				max = Integer.parseInt(capacity);
				seat = 1;
				row = 1;
				gym = 0;
				String room = Info[index][RoomIndex][0];
				if (room.contains("GYM")) {
					gym = 1;
					txtRow.setText(row+"");

				}
				tabFolder.setSelection(2);
				btnNext.setEnabled(true);
				PrintRm();
				btnTest.setEnabled(false);
			}
		});
		btnTest.setBounds(28, 121, 104, 58);
		btnTest.setText("Start");


		
		tbtmDate = new TabItem(tabFolder, SWT.NONE);
		tbtmDate.setText("Date");
		
		dateTime = new DateTime(tabFolder, SWT.BORDER | SWT.CALENDAR);
		tbtmDate.setControl(dateTime);
		dateTime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Calendar instance = Calendar.getInstance();
				instance.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
				instance.set(Calendar.MONTH, dateTime.getMonth());
				instance.set(Calendar.YEAR, dateTime.getYear());
				dateString = new SimpleDateFormat("ddMMMyy").format(instance.getTime());
				dateString = dateString.toUpperCase();
			}
		});
		
		TabItem tbtmInformation = new TabItem(tabFolder, SWT.NONE);
		tbtmInformation.setText("Information");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmInformation.setControl(composite);
		
		Label lbl1 = new Label(composite, SWT.NONE);
		lbl1.setAlignment(SWT.CENTER);
		lbl1.setBounds(14, 10, 74, 15);
		lbl1.setText("Building");
		
		Label lbl2 = new Label(composite, SWT.NONE);
		lbl2.setAlignment(SWT.CENTER);
		lbl2.setBounds(102, 10, 37, 15);
		lbl2.setText("Code");
		
		Label lbl3 = new Label(composite, SWT.NONE);
		lbl3.setAlignment(SWT.CENTER);
		lbl3.setBounds(153, 10, 55, 15);
		lbl3.setText("Room");
		
		Label lblMaxCap = new Label(composite, SWT.NONE);
		lblMaxCap.setAlignment(SWT.CENTER);
		lblMaxCap.setBounds(222, 10, 41, 15);
		lblMaxCap.setText("Max Cap.");

		lblBuilding = new Label(composite, SWT.NONE);
		lblBuilding.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblBuilding.setAlignment(SWT.CENTER);
		lblBuilding.setBounds(14, 42, 74, 43);
		
		lblCode = new Label(composite, SWT.NONE);
		lblCode.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblCode.setAlignment(SWT.CENTER);
		lblCode.setBounds(102, 42, 37, 43);
		
		lblRm = new Label(composite, SWT.NONE);
		lblRm.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblRm.setAlignment(SWT.CENTER);
		lblRm.setBounds(153, 42, 55, 43);
		
		lblCapacity = new Label(composite, SWT.NONE);
		lblCapacity.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblCapacity.setAlignment(SWT.CENTER);
		lblCapacity.setBounds(222, 42, 41, 43);
		
		lblDate = new Label(composite, SWT.NONE);
		lblDate.setBounds(10, 91, 186, 15);
		
		TabItem tbtmOutput = new TabItem(tabFolder, SWT.NONE);
		tbtmOutput.setText("Output");
		
		composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmOutput.setControl(composite_1);
		formToolkit.paintBordersFor(composite_1);
		
		lblOutput = new Label(composite_1, SWT.NONE);
		lblOutput.setLocation(0, 0);
		lblOutput.setSize(281, 48);
		lblOutput.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblOutput.setAlignment(SWT.CENTER);
		
		btnNext = new Button(composite_1, SWT.NONE);
		btnNext.setEnabled(false);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PrintRm();
			}
		});
		btnNext.setBounds(87, 102, 106, 25);
		formToolkit.adapt(btnNext, true, true);
		btnNext.setText("Print Next");
		
		Label lblRow = new Label(composite_1, SWT.NONE);
		lblRow.setAlignment(SWT.CENTER);
		lblRow.setBounds(94, 57, 41, 15);
		formToolkit.adapt(lblRow, true, true);
		lblRow.setText("Row");
		
		txtRow = new Text(composite_1, SWT.BORDER);
		txtRow.setBounds(141, 54, 28, 21);
		formToolkit.adapt(txtRow, true, true);
		
		lblRm_1 = new Label(composite_1, SWT.NONE);
		lblRm_1.setText("Rm");
		lblRm_1.setAlignment(SWT.CENTER);
		lblRm_1.setBounds(94, 81, 41, 15);
		formToolkit.adapt(lblRm_1, true, true);
		
		txtRm = new Text(composite_1, SWT.BORDER);
		txtRm.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				try {seat = Integer.parseInt(txtRm.getText());
						} catch(Exception D) {}
			}
		});
		txtRm.setBounds(141, 78, 28, 21);
		formToolkit.adapt(txtRm, true, true);

		
		Input2 = new Combo(shell, SWT.READ_ONLY);
		Input2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RoomIndex = Input2.getSelectionIndex();
				refreshOptions();
			}
		});
		Input2.setEnabled(true);
		Input2.setBounds(28, 80, 91, 23);
		
		Input = new Combo(shell, SWT.READ_ONLY);
		Input.setBackground(SWTResourceManager.getColor(240, 255, 240));
		Input.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Input2.removeAll();
				index = Input.getSelectionIndex();
				int a = 0;
				while(Info[index][a][0] != null) {
					System.out.println(Info[index][a][0]);
					Input2.add(Info[index][a][0],a);
					a++;
				}
				Input2.select(0);
				refreshOptions();
			}
		});
		Input.setBounds(28, 36, 91, 23);
		Input.setItems(Name);
		
		Button btnReset = new Button(shell, SWT.NONE);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnTest.setEnabled(true);
				btnNext.setEnabled(false);
				tabFolder.setSelection(0);
			}
		});
		btnReset.setBounds(349, 202, 75, 25);
		formToolkit.adapt(btnReset, true, true);
		btnReset.setText("Reset");
				

	}
}
