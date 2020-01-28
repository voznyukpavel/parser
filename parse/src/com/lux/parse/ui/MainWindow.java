package com.lux.parse.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MainWindow {
    private static final String FILE_SEPARATOR = "<<<NEXT_FILE>>>";
    private static final String EXPRESSION_SEPARATOR = "<<<NEXT>>>";

    private static final String CLEAN = "Clean";
    private static final String CHANGE = "Change";
    private static final String SAVE = "Save";
    private static final String LOAD = "Load";
    private static final String NEXT = "Next";
    private static final String NEXT_FILE = "Next file";

    private static final String FILES_LIST = "Files list:";
    private static final String PATH = "Repo path:";
    private static final String FROM = "From:";
    private static final String TO = "To:";

    private static final String NAME = "Parse";
    private static final int MIN_WINDOW_HEIGHT = 430;
    private static final int MIN_WINDOW_WIDTH = 720;
    private static final int WINDOW_HEIGHT = 430;
    private static final int WINDOW_WIDTH = 720;

    private Display display;
    private Shell shell;
    private Text fileListTextArea, pathText, fromTextArea, toTextArea;
    private Button saveButton, loadButton, cleanButton, chengeButton, nextButton, nextFileButton;

    public void open() {
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

        fromTextArea = new Text(changeFromComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        fromTextArea.setLayoutData(sashGridData);

        Composite changeToComposite = new Composite(changeSashForm, SWT.NONE);
        changeToComposite.setLayout(textGridLayout);

        Label toLabel = new Label(changeToComposite, SWT.BEGINNING);
        toLabel.setText(TO);
        toLabel.setFont(font);

        toTextArea = new Text(changeToComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        toTextArea.setLayoutData(sashGridData);

        initControlButtons(dataCompoiste);

        Label separatorLabel2 = new Label(dataCompoiste, SWT.HORIZONTAL | SWT.SEPARATOR);
        separatorLabel2.setLayoutData(lineSeparatorGridData);

        initButton(dataCompoiste);
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
                replaceToken(FILE_SEPARATOR, EXPRESSION_SEPARATOR);
            }

        });

        nextFileButton = new Button(nextButtonsComposite, SWT.PUSH);
        nextFileButton.setText(NEXT_FILE);
        nextFileButton.setLayoutData(gridDataButton);
        nextFileButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                replaceToken(EXPRESSION_SEPARATOR, FILE_SEPARATOR);
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

    private void replaceToken(String from_exp, String to_exp) {
        String from = replaceToken(fromTextArea, toTextArea, from_exp, to_exp);
        String to = replaceToken(toTextArea, fromTextArea, from_exp, to_exp);
        setTextArea(from, to);
    }

    private String replaceToken(Text textAreaChange, Text textAreaCheck, String from_exp, String to_exp) {
        String stringChanged = textAreaChange.getText().trim();
        String stringChecked = textAreaCheck.getText().trim();
        String lineSeparator = System.lineSeparator();
        if (stringChanged.isEmpty() && stringChecked.isEmpty()) {
            return "";
        } else if (stringChanged.endsWith(to_exp) && stringChecked.endsWith(to_exp)) {
            return stringChanged;
        } else if (stringChanged.endsWith(from_exp) && stringChecked.endsWith(from_exp)) {
            int index = from_exp.length();
            stringChanged = stringChanged.substring(0, stringChanged.length() - index) + to_exp;
            return stringChanged;
        } else {
            if (!stringChanged.isEmpty()) {
                stringChanged = stringChanged + lineSeparator + to_exp;
            } else
                stringChanged = stringChanged + to_exp;
            return stringChanged;
        }
    }

    private void setTextArea(String from, String to) {
        fromTextArea.setText(from);
        toTextArea.setText(to);
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

        loadButton = new Button(inputDataComposite, SWT.PUSH);
        loadButton.setText(LOAD);
        loadButton.setLayoutData(buttonGridData);

        chengeButton = new Button(inputDataComposite, SWT.PUSH);
        chengeButton.setText(CHANGE);
        chengeButton.setLayoutData(buttonGridData);
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
