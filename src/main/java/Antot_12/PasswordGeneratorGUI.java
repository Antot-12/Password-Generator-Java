package Antot_12;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

public class PasswordGeneratorGUI extends JFrame {

    private static final int MAX_PASSWORD_LENGTH = 32;
    private JTextField lengthField;
    private JCheckBox upperCaseCheck;
    private JCheckBox digitsCheck;
    private JCheckBox specialCharCheck;
    private JLabel resultLabel;
    private JLabel lengthLabel; // Перенесено на рівень класу
    private ResourceBundle bundle;
    private JButton generateButton;
    private JButton copyButton;

    public PasswordGeneratorGUI() {
        // Ініціалізація GUI компонентів
        bundle = ResourceBundle.getBundle("Antot_12.messages", Locale.getDefault());

        setTitle(bundle.getString("title"));
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(44, 44, 44)); // Темний фон
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Поле введення довжини пароля
        lengthLabel = createLabel(bundle.getString("length_label")); // Оголошено в класі
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(lengthLabel, gbc);

        lengthField = new JTextField();
        lengthField.setBackground(new Color(60, 63, 65));
        lengthField.setForeground(Color.WHITE);
        lengthField.setFont(new Font("Arial", Font.PLAIN, 20));  // Збільшений текст
        lengthField.setToolTipText(String.format(bundle.getString("length_tooltip"), MAX_PASSWORD_LENGTH));
        lengthField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { validateInput(); }
            public void removeUpdate(DocumentEvent e) { validateInput(); }
            public void insertUpdate(DocumentEvent e) { validateInput(); }

            private void validateInput() {
                SwingUtilities.invokeLater(() -> {
                    String text = lengthField.getText();
                    if (!text.matches("\\d*")) {
                        lengthField.setText(text.replaceAll("[^\\d]", ""));
                    }
                });
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lengthField, gbc);

        // Панель для чекбоксів
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setBackground(new Color(44, 44, 44));
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

        // Чекбокси для вибору опцій
        upperCaseCheck = new JCheckBox(bundle.getString("include_upper"));
        styleCheckBox(upperCaseCheck);
        checkBoxPanel.add(upperCaseCheck);

        digitsCheck = new JCheckBox(bundle.getString("include_digits"));
        styleCheckBox(digitsCheck);
        checkBoxPanel.add(digitsCheck);

        specialCharCheck = new JCheckBox(bundle.getString("include_special"));
        styleCheckBox(specialCharCheck);
        checkBoxPanel.add(specialCharCheck);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        add(checkBoxPanel, gbc);

        // Результат генерації пароля
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setForeground(new Color(0, 191, 174));
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));  // Збільшений текст
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        add(resultLabel, gbc);

        // Риска для відділення кнопок
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(0, 191, 174));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        add(separator, gbc);

        // Кнопка для генерації пароля
        generateButton = new JButton(bundle.getString("generate_button"));
        styleButton(generateButton);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePassword();
            }
        });

        // Кнопка для копіювання пароля
        copyButton = new JButton(bundle.getString("copy_button"));
        styleButton(copyButton);
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyToClipboard(resultLabel.getText());
            }
        });

        // Розміщення кнопок в ряд
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(44, 44, 44)); // Темний фон
        buttonPanel.add(generateButton);
        buttonPanel.add(copyButton);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        add(buttonPanel, gbc);

        // Вибір мови
        JPanel languagePanel = new JPanel();
        languagePanel.setBackground(new Color(44, 44, 44)); // Темний фон
        JButton enLangButton = createLanguageButton("EN", Locale.ENGLISH);
        JButton uaLangButton = createLanguageButton("UA", new Locale("uk", "UA"));
        JButton skLangButton = createLanguageButton("SK", new Locale("sk", "SK"));
        languagePanel.add(enLangButton);
        languagePanel.add(uaLangButton);
        languagePanel.add(skLangButton);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        add(languagePanel, gbc);

        setVisible(true);
    }

    private void generatePassword() {
        try {
            int length = Integer.parseInt(lengthField.getText());
            if (length <= 0 || length > MAX_PASSWORD_LENGTH) {
                JOptionPane.showMessageDialog(this, bundle.getString("error_message"));
                return;
            }

            boolean includeUpperCase = upperCaseCheck.isSelected();
            boolean includeDigits = digitsCheck.isSelected();
            boolean includeSpecialChar = specialCharCheck.isSelected();

            if (!includeUpperCase && !includeDigits && !includeSpecialChar) {
                JOptionPane.showMessageDialog(this, bundle.getString("error_no_options"));
                return;
            }

            String password = PasswordGenerator.generate(length, includeUpperCase, includeDigits, includeSpecialChar);
            resultLabel.setText(password);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, bundle.getString("error_message"));
        }
    }

    private void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }

    private void switchLanguage(Locale locale) {
        Locale.setDefault(locale);
        bundle = ResourceBundle.getBundle("Antot_12.messages", locale);
        setTitle(bundle.getString("title"));
        upperCaseCheck.setText(bundle.getString("include_upper"));
        digitsCheck.setText(bundle.getString("include_digits"));
        specialCharCheck.setText(bundle.getString("include_special"));
        lengthLabel.setText(bundle.getString("length_label")); // Оновлення тексту лейблу
        lengthField.setToolTipText(String.format(bundle.getString("length_tooltip"), MAX_PASSWORD_LENGTH));
        generateButton.setText(bundle.getString("generate_button"));
        copyButton.setText(bundle.getString("copy_button"));
        resultLabel.setText("");
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setForeground(new Color(0, 191, 174)); // Бірюзовий текст
        label.setFont(new Font("Arial", Font.BOLD, 20)); // Збільшений текст
        return label;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 191, 174));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18)); // Збільшений текст
    }

    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setBackground(new Color(44, 44, 44));
        checkBox.setForeground(new Color(0, 191, 174)); // Бірюзовий текст
        checkBox.setFocusPainted(false);
        checkBox.setFont(new Font("Arial", Font.BOLD, 20)); // Збільшений текст
        checkBox.setIcon(createCustomCheckBoxIcon()); // Додання оранжевого кольору
    }

    private Icon createCustomCheckBoxIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                JCheckBox checkBox = (JCheckBox) c;
                if (checkBox.isSelected()) {
                    g.setColor(Color.ORANGE);
                    g.fillRect(x, y, getIconWidth(), getIconHeight());
                } else {
                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, getIconWidth(), getIconHeight());
                }
            }

            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }
        };
    }

    private JButton createLanguageButton(String text, Locale locale) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 191, 174));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18)); // Збільшений текст
        button.addActionListener(e -> switchLanguage(locale));
        return button;
    }

    public static void main(String[] args) {
        new PasswordGeneratorGUI();
    }
}
