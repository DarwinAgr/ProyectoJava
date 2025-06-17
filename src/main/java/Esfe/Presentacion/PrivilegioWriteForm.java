package Esfe.Presentacion;

import Esfe.Dominio.Privilegio;
import Esfe.Persistencia.PrivilegioDAO;
import Esfe.Utils.CBOption;
import Esfe.Utils.CUD;

import javax.swing.*;

public class PrivilegioWriteForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtDescription;
    private JComboBox cbStatus;
    private JButton btnOk;
    private JButton btnCancel;

    private PrivilegioDAO privilegioDAO;
    private MainForm mainForm;
    private CUD cud;
    private Privilegio privilegio;

    public PrivilegioWriteForm(MainForm mainForm, CUD cud, Privilegio privilegio) {
        this.mainForm = mainForm;
        this.cud = cud;
        this.privilegio = privilegio;
        this.privilegioDAO = new PrivilegioDAO();

        setContentPane(mainPanel);
        setModal(true);
        init();
        pack();
        setLocationRelativeTo(mainForm);

        btnCancel.addActionListener(s -> this.dispose());
        btnOk.addActionListener(s -> ok());
    }

    private void init() {
        initCBStatus();

        switch (this.cud) {
            case CREATE:
                setTitle("Crear Privilegio");
                btnOk.setText("Guardar");
                break;
            case UPDATE:
                setTitle("Modificar Privilegio");
                btnOk.setText("Guardar");
                break;
            case DELETE:
                setTitle("Eliminar Privilegio");
                btnOk.setText("Eliminar");
                break;
        }

        setValuesControls(this.privilegio);
    }

    private void initCBStatus() {
        DefaultComboBoxModel<CBOption> model = new DefaultComboBoxModel<>();
        cbStatus.setModel(model);
        model.addElement(new CBOption("ACTIVO", (byte) 1));
        model.addElement(new CBOption("INACTIVO", (byte) 2));
    }

    private void setValuesControls(Privilegio privilegio) {
        txtName.setText(privilegio.getName());
        txtDescription.setText(privilegio.getDescription());
        cbStatus.setSelectedItem(new CBOption(null, privilegio.getStatus()));

        if (this.cud == CUD.CREATE) {
            cbStatus.setSelectedItem(new CBOption(null, 1));
        }

        if (this.cud == CUD.DELETE) {
            txtName.setEditable(false);
            txtDescription.setEditable(false);
            cbStatus.setEnabled(false);
        }
    }

    private boolean getValuesControls() {
        boolean res = false;
        CBOption selectedOption = (CBOption) cbStatus.getSelectedItem();
        byte status = selectedOption != null ? (byte) selectedOption.getValue() : 0;

        if (txtName.getText().trim().isEmpty() ||
                txtDescription.getText().trim().isEmpty() ||
                status == 0 ||
                (this.cud != CUD.CREATE && this.privilegio.getIdPrivilegio() == 0)) {
            return res;
        }

        this.privilegio.setName(txtName.getText());
        this.privilegio.setDescription(txtDescription.getText());
        this.privilegio.setStatus(status);
        res = true;
        return res;
    }

    private void ok() {
        try {
            boolean res = getValuesControls();

            if (res) {
                boolean r = false;
                switch (this.cud) {
                    case CREATE:
                        Privilegio nuevo = privilegioDAO.create(this.privilegio);
                        if (nuevo.getIdPrivilegio() > 0) {
                            r = true;
                        }
                        break;
                    case UPDATE:
                        r = privilegioDAO.update(this.privilegio);
                        break;
                    case DELETE:
                        r = privilegioDAO.delete(this.privilegio);
                        break;
                }

                if (r) {
                    JOptionPane.showMessageDialog(null,
                            "Transacción realizada exitosamente",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "No se logró realizar ninguna acción",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Los campos con * son obligatorios",
                        "Validación", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}

