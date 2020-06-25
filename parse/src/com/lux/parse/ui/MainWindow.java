package com.lux.parse.ui;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.json.simple.parser.ParseException;

import com.lux.parse.exceptions.FromToParseException;
import com.lux.parse.manager.DataManager;
import com.lux.parse.util.ParserExpressionConstants;

/**
 * Contains all UI components
 *
 */

public class MainWindow {

	// Error messages
	private static final String IOERROR = "I/O Error";
	private static final String MESSAGE_FILE_READ_ERROR = "Error occured while file was reading";
	private static final String MESSAGE_FILE_WRITE_ERROR = "Error occured while file was writing";
	private static final String PARSE_ERROR = "Parse error";
	private static final String COUNT_OF_TOKENS_DOESNT_MUTCH = "Number of tokens or order in \"from\" does not mutch with \"to\"";

	private static final String OPEN = "Open";
	private static final String CLEAN = "Clean";
	private static final String CHANGE = "Change";
	private static final String SAVE = "Save";
	private static final String LOAD = "Load";
	private static final String NEXT = "Next";
	private static final String NEXT_FILE = "Next file";

	private static final String FILES_LIST = "Files list:";
	private static final String PATH = "Repo path:";
	private static final String SKIP = "Skip:";
	private static final String FROM = "From:";
	private static final String TO = "To:";

	private static final int EXPRESSION_SPLITER_COLOR = SWT.COLOR_DARK_BLUE;
	private static final int FILE_SPLITER_COLOR = SWT.COLOR_DARK_GREEN;

	private static final String NAME = "Parse";
	private static final int MIN_WINDOW_HEIGHT = 430;
	private static final int MIN_WINDOW_WIDTH = 720;
	private static final int WINDOW_HEIGHT = 500;
	private static final int WINDOW_WIDTH = 720;

	private Display display;
	private Shell shell;
	private Text fileListTextArea, skipText, pathText;
	private StyledText fromTextArea, toTextArea;
	private Button saveButton, loadButton, cleanButton, chengeButton, nextButton, nextFileButton;
	private DataManager dataManager;

	/**
	 * Creates window and initializes components
	 *
	 */
	public void open() {
		dataManager = new DataManager();
		initShell();
		initDataUI();
		openWindow();
	}

	private void initDataUI() {
		Composite dataCompoiste = new Composite(shell, SWT.NONE);
		dataCompoiste.setLayout(new GridLayout(1, true));
		SashForm sashForm = new SashForm(dataCompoiste, SWT.VERTICAL | SWT.SMOOTH);

		GridData sashGridData = new GridData(GridData.FILL_BOTH);
		sashForm.setLayoutData(sashGridData);

		Composite filesListComposite = new Composite(sashForm, SWT.FILL);
		GridLayout textGridLayout = new GridLayout(1, true);
		filesListComposite.setLayout(textGridLayout);

		Label pathLabel = new Label(filesListComposite, SWT.BEGINNING);
		FontData fontData = pathLabel.getFont().getFontData()[0];
		Font font = new Font(null, new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		pathLabel.setText(PATH);
		pathLabel.setFont(font);

		GridData addressTextGridData = new GridData(GridData.FILL, GridData.FILL, true, false);

		pathText = new Text(filesListComposite, SWT.BORDER | SWT.LEFT);
		pathText.setLayoutData(addressTextGridData);

		Label skipLabel = new Label(filesListComposite, SWT.BEGINNING);
		skipLabel.setText(SKIP);
		skipLabel.setFont(font);

		skipText = new Text(filesListComposite, SWT.BORDER | SWT.LEFT);
		skipText.setLayoutData(addressTextGridData);

		Label filesListLabel = new Label(filesListComposite, SWT.BEGINNING);
		filesListLabel.setText(FILES_LIST);
		filesListLabel.setFont(font);

		fileListTextArea = new Text(filesListComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		fileListTextArea.setLayoutData(sashGridData);

		Composite changeComposite = new Composite(sashForm, SWT.FILL);
		changeComposite.setLayout(textGridLayout);

		Label changeLabel = new Label(changeComposite, SWT.BEGINNING);
		changeLabel.setText(CHANGE);
		changeLabel.setFont(font);

		GridData lineSeparatorGridData = new GridData(GridData.FILL_HORIZONTAL);

		Label separatorLabel = new Label(changeComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
		separatorLabel.setLayoutData(lineSeparatorGridData);

		SashForm changeSashForm = new SashForm(changeComposite, SWT.HORIZONTAL | SWT.SMOOTH);
		changeSashForm.setLayoutData(sashGridData);

		Composite changeFromComposite = new Composite(changeSashForm, SWT.NONE);
		changeFromComposite.setLayout(textGridLayout);

		Label fromLabel = new Label(changeFromComposite, SWT.BEGINNING);
		fromLabel.setText(FROM);
		fromLabel.setFont(font);

		fromTextArea = new StyledText(changeFromComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		fromTextArea.setLayoutData(sashGridData);
		addDefaultContextMenu(fromTextArea);
		fromTextArea.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event e) {
				setColorsToText(fromTextArea);
			}
		});

		Composite changeToComposite = new Composite(changeSashForm, SWT.NONE);
		changeToComposite.setLayout(textGridLayout);

		Label toLabel = new Label(changeToComposite, SWT.BEGINNING);
		toLabel.setText(TO);
		toLabel.setFont(font);

		toTextArea = new StyledText(changeToComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		toTextArea.setLayoutData(sashGridData);
		addDefaultContextMenu(toTextArea);
		toTextArea.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event e) {
				setColorsToText(toTextArea);
			}
		});

		initControlButtons(dataCompoiste);

		Label separatorLabel2 = new Label(dataCompoiste, SWT.HORIZONTAL | SWT.SEPARATOR);
		separatorLabel2.setLayoutData(lineSeparatorGridData);

		initButton(dataCompoiste);
	}

	private static void addDefaultContextMenu(final StyledText control) {
		Menu menu = new Menu(control);
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText("Cut");
		item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				control.cut();
			}
		});
		item = new MenuItem(menu, SWT.PUSH);
		item.setText("Copy");
		item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				control.copy();
			}
		});
		item = new MenuItem(menu, SWT.PUSH);
		item.setText("Paste");
		item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				control.paste();
			}
		});
		item = new MenuItem(menu, SWT.PUSH);
		item.setText("Select All");
		item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				control.selectAll();
			}
		});
		control.setMenu(menu);
	}

	private void setColorsToText(StyledText text) {
		setColorForExpression(text, EXPRESSION_SPLITER_COLOR, ParserExpressionConstants.EXPRESSION_SEPARATOR);
		setColorForExpression(text, FILE_SPLITER_COLOR, ParserExpressionConstants.FILE_SEPARATOR);
	}

	private void setColorForExpression(StyledText text, int colorNum, String expression) {
		StyleRange style = new StyleRange();
		int length = expression.length();
		String string = text.getText();
		int stringLength = string.length();
		style.foreground = display.getSystemColor(colorNum);
		style.length = length;
		for (int i = 0; i < stringLength - length; i++) {
			int index = string.indexOf(expression, i);
			if (index != -1) {
				style.start = index;
				i = index + length;
				text.setStyleRange(style);
			} else {
				break;
			}
		}
	}

	private void initControlButtons(Composite dataCompoiste) {
		Composite nextButtonsComposite = new Composite(dataCompoiste, SWT.NONE);
		nextButtonsComposite.setLayout(new GridLayout(3, false));

		GridData gridDataButtonComposit = new GridData();
		gridDataButtonComposit.horizontalAlignment = SWT.BEGINNING;
		nextButtonsComposite.setLayoutData(gridDataButtonComposit);

		GridData gridDataButton = new GridData();
		gridDataButton.widthHint = 80;
		gridDataButton.heightHint = 20;
		gridDataButton.horizontalAlignment = SWT.BEGINNING;

		nextButton = new Button(nextButtonsComposite, SWT.PUSH);
		nextButton.setText(NEXT);
		nextButton.setLayoutData(gridDataButton);
		nextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setToken(ParserExpressionConstants.FILE_SEPARATOR, ParserExpressionConstants.EXPRESSION_SEPARATOR);
				fromTextArea.setTopIndex(fromTextArea.getLineCount() - 1);
				toTextArea.setTopIndex(toTextArea.getLineCount() - 1);
			}

		});

		nextFileButton = new Button(nextButtonsComposite, SWT.PUSH);
		nextFileButton.setText(NEXT_FILE);
		nextFileButton.setLayoutData(gridDataButton);
		nextFileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setToken(ParserExpressionConstants.EXPRESSION_SEPARATOR, ParserExpressionConstants.FILE_SEPARATOR);
				fromTextArea.setTopIndex(fromTextArea.getLineCount() - 1);
				toTextArea.setTopIndex(toTextArea.getLineCount() - 1);
			}

		});

		cleanButton = new Button(nextButtonsComposite, SWT.PUSH);
		cleanButton.setText(CLEAN);
		cleanButton.setLayoutData(gridDataButton);
		cleanButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setTextArea("", "");
			}
		});
	}

	private void initButton(Composite dataCompoiste) {
		Composite inputDataComposite = new Composite(dataCompoiste, SWT.NONE);
		inputDataComposite.setLayout(new GridLayout(3, true));

		GridData compositeData = new GridData();
		compositeData.horizontalAlignment = GridData.END;
		inputDataComposite.setLayoutData(compositeData);

		GridData buttonGridData = new GridData();
		buttonGridData.widthHint = 80;
		buttonGridData.heightHint = 30;

		saveButton = new Button(inputDataComposite, SWT.PUSH);
		saveButton.setText(SAVE);
		saveButton.setLayoutData(buttonGridData);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				save();
			}
		});

		loadButton = new Button(inputDataComposite, SWT.PUSH);
		loadButton.setText(LOAD);
		loadButton.setLayoutData(buttonGridData);
		loadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				load();
			}
		});

		chengeButton = new Button(inputDataComposite, SWT.PUSH);
		chengeButton.setText(CHANGE);
		chengeButton.setLayoutData(buttonGridData);
		chengeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getData();
			}
		});
	}

	private void save() {
		File file = createFileDialog(SAVE, new String[] { "*.json" }, SWT.SAVE);
		try {
			if (file != null) {
				saveData(file);
			}
		} catch (IOException e) {
			MessageDialog.openError(shell, IOERROR, file.getName() + ": " + MESSAGE_FILE_WRITE_ERROR);
		} catch (FromToParseException e) {
			e.printStackTrace();
		}
	}

	private void load() {
		File file = createFileDialog(OPEN, new String[] { "*.json" }, SWT.OPEN);
		try {
			if (file != null) {
				loadData(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(shell, IOERROR, file.getName() + ": " + MESSAGE_FILE_READ_ERROR);
		}
	}

	private File createFileDialog(String action, String[] filter, int swtType) {
		FileDialog fd = new FileDialog(shell, swtType);
		fd.setText(action);
		fd.setFilterExtensions(filter);
		File file = null;
		if (fd.open() != null) {
			file = new File(fd.getFilterPath() + "\\" + fd.getFileName());
		}
		return file;
	}

	private void saveData(File file) throws IOException, FromToParseException {
		String fileNames = fileListTextArea.getText().trim();
		String path = pathText.getText().trim();
		String skip = skipText.getText().trim();
		String from = fromTextArea.getText().trim();
		String to = toTextArea.getText().trim();
		dataManager = new DataManager(path, skip, fileNames, from, to);
		dataManager.saveToFile(file);

	}

	private void loadData(File file) throws IOException, ParseException {
		dataManager.loadFromFile(file);
		pathText.setText(dataManager.getPath());
		skipText.setText(dataManager.getSkip());
		fileListTextArea.setText(dataManager.getFileNames());
		fromTextArea.setText(dataManager.getFrom());
		toTextArea.setText(dataManager.getTo());
	}

	private void getData() {
		String fileNames = fileListTextArea.getText().trim();
		String path = pathText.getText().trim();
		String skip = skipText.getText().trim();
		String from = fromTextArea.getText().trim();
		String to = toTextArea.getText().trim();
		try {
			dataManager = new DataManager(path, skip, fileNames, from, to);
			dataManager.parse();
		} catch (FromToParseException ftpe) {
			MessageDialog.openError(shell, PARSE_ERROR, COUNT_OF_TOKENS_DOESNT_MUTCH);
		} catch (IOException e) {
			MessageDialog.openError(shell, IOERROR, IOERROR);

		}
	}

	private void setToken(String from_exp, String to_exp) {
		String from = replaceToken(fromTextArea, toTextArea, from_exp, to_exp);
		String to = replaceToken(toTextArea, fromTextArea, from_exp, to_exp);
		setTextArea(from, to);
	}

	private String replaceToken(StyledText fromTextArea2, StyledText toTextArea2, String from_exp, String to_exp) {
		String stringChanged = fromTextArea2.getText().trim();
		String stringChecked = toTextArea2.getText().trim();
		String lineSeparator = System.lineSeparator();
		if (stringChanged.isEmpty() && stringChecked.isEmpty()) {
			return "";
		} else if (stringChanged.endsWith(to_exp) && stringChecked.endsWith(to_exp)) {
			return stringChanged + lineSeparator;
		} else if (stringChanged.endsWith(from_exp) && stringChecked.endsWith(from_exp)) {
			int index = from_exp.length();
			stringChanged = stringChanged.substring(0, stringChanged.length() - index) + to_exp + lineSeparator;
			return stringChanged;
		} else {
			if (!stringChanged.isEmpty()) {
				stringChanged = stringChanged + lineSeparator + to_exp + lineSeparator;
			} else {
				stringChanged = stringChanged + to_exp + lineSeparator;
			}
			return stringChanged;
		}
	}

	private void setTextArea(String from, String to) {
		fromTextArea.setText(from);
		toTextArea.setText(to);
	}

	private void initShell() {
		display = new Display();
		shell = new Shell(display);
		shell.setText(NAME);
		shell.setMinimumSize(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT);
		shell.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		shell.setLayout(new FillLayout());
	}

	private void openWindow() {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
