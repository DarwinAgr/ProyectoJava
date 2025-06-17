package Esfe.Presentacion;

import Esfe.Dominio.Privilegio;
import Esfe.Persistencia.PrivilegioDAO;
import Esfe.Utils.CUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PrivilegioReadingForm extends JDialog {
    private JPanel mainPanel;
    private JLabel nombre;
    private JButton btnCreate;
    private JTable tbPrivilegios;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JTextField txtName;

    private PrivilegioDAO privilegioDAO;
    private MainForm mainForm;

    public PrivilegioReadingForm(MainForm mainForm) {
        this.mainForm = mainForm;
        privilegioDAO = new PrivilegioDAO();
        setContentPane(mainPanel);
        setModal(true);
        setTitle("Buscar Privilegio");
        pack();
        setLocationRelativeTo(mainForm);

        txtName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!txtName.getText().trim().isEmpty()) {
                    search(txtName.getText());
                } else {
                    DefaultTableModel emptyModel = new DefaultTableModel();
                    tbPrivilegios.setModel(emptyModel);
                }
            }
        });

        btnCreate.addActionListener(s -> {
            PrivilegioWriteForm writeForm = new PrivilegioWriteForm(this.mainForm, CUD.CREATE, new Privilegio());
            writeForm.setVisible(true);
            tbPrivilegios.setModel(new DefaultTableModel());
        });

        btnUpdate.addActionListener(s -> {
            Privilegio privilegio = getPrivilegioFromTableRow();
            if (privilegio != null) {
                PrivilegioWriteForm writeForm = new PrivilegioWriteForm(this.mainForm, CUD.UPDATE, privilegio);
                writeForm.setVisible(true);
                tbPrivilegios.setModel(new DefaultTableModel());
            }
        });

        btnDelete.addActionListener(s -> {
            Privilegio privilegio = getPrivilegioFromTableRow();
            if (privilegio != null) {
                PrivilegioWriteForm writeForm = new PrivilegioWriteForm(this.mainForm, CUD.DELETE, privilegio);
                writeForm.setVisible(true);
                tbPrivilegios.setModel(new DefaultTableModel());
            }
        });
    }

    private void search(String query) {
        try {
            ArrayList<Privilegio> privilegios = privilegioDAO.search(query);
            createTable(privilegios);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTable(ArrayList<Privilegio> privilegios) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Id");
        model.addColumn("Nombre");
        model.addColumn("Descripcion");
        model.addColumn("Status");

        this.tbPrivilegios.setModel(model);

        Object row[] = null;

        for (int i = 0; i < privilegios.size(); i++) {
            Privilegio privilegio = privilegios.get(i);
            model.addRow(row);
            model.setValueAt(privilegio.getIdPrivilegio(), i, 0);
            model.setValueAt(privilegio.getName(), i, 1);
            model.setValueAt(privilegio.getDescription(), i, 2);
            model.setValueAt(privilegio.getStatus(), i, 3);
        }

        hideCol(0);
    }

    private void hideCol(int pColumna) {
        this.tbPrivilegios.getColumnModel().getColumn(pColumna).setMaxWidth(0);
        this.tbPrivilegios.getColumnModel().getColumn(pColumna).setMinWidth(0);
        this.tbPrivilegios.getTableHeader().getColumnModel().getColumn(pColumna).setMaxWidth(0);
        this.tbPrivilegios.getTableHeader().getColumnModel().getColumn(pColumna).setMinWidth(0);
    }

    private Privilegio getPrivilegioFromTableRow() {
        Privilegio privilegio = null;
        try {
            int filaSelect = this.tbPrivilegios.getSelectedRow();
            int id = 0;

            if (filaSelect != -1) {
                id = (int) this.tbPrivilegios.getValueAt(filaSelect, 0);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Seleccionar una fila de la tabla.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            privilegio = privilegioDAO.getById(id);

            if (privilegio.getIdPrivilegio() == 0) {
                JOptionPane.showMessageDialog(null,
                        "No se encontró ningún privilegio.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            return privilegio;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}

