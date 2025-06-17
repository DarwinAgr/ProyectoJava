package Esfe.Presentacion;

import Esfe.Persistencia.UsuarioDAO; // Importa la interfaz o clase UserDAO, que define las operaciones de acceso a datos para la entidad User.
import Esfe.Utils.CBOption; // Importa la clase CBOption, probablemente una clase utilitaria para manejar opciones de un ComboBox (por ejemplo, para asociar un valor con un texto).
import Esfe.Utils.CUD; // Importa el enum CUD (Create, Update, Delete),  para indicar el tipo de operación que se está realizando (Crear, Actualizar, Eliminar).
import javax.swing.*; // Importa el paquete Swing, que proporciona clases para crear interfaces gráficas de usuario (GUIs).
import Esfe.Dominio.Usuario; // Importa la clase User, que representa la entidad de usuario en el dominio de la aplicación.


public class UserWriteForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox cbNivelUsuario;
    private JComboBox cbStatus;
    private JButton btnOk;
    private JButton btnCancel;
    private JLabel lbPassword;
    private JTextField txtNivelUsuario;

    private UsuarioDAO userDAO; // Instancia de la clase UserDAO para interactuar con la base de datos de usuarios.
    private MainForm mainForm; // Referencia a la ventana principal de la aplicación.
    private CUD cud; // Variable para almacenar el tipo de operación (Create, Update, Delete) que se está realizando en este formulario.
    private Usuario en; // Variable para almacenar el objeto User que se está creando, actualizando o eliminando.


    // Constructor de la clase UserWriteForm. Recibe la ventana principal, el tipo de operación CUD y un objeto User como parámetros.
    public UserWriteForm(MainForm mainForm, CUD cud, Usuario user) {
        this.cud = cud; // Asigna el tipo de operación CUD recibida a la variable local 'cud'.
        this.en = user; // Asigna el objeto User recibido a la variable local 'en'.
        this.mainForm = mainForm; // Asigna la instancia de MainForm recibida a la variable local 'mainForm'.
        userDAO = new UsuarioDAO(); // Crea una nueva instancia de UserDAO al instanciar este formulario.
        setContentPane(mainPanel); // Establece el panel principal como el contenido de este diálogo.
        setModal(true); // Hace que este diálogo sea modal, bloqueando la interacción con la ventana principal hasta que se cierre.
        init(); // Llama al método 'init' para inicializar y configure the form based on 'cud'
        pack(); // Ajusta el tamaño de la ventana para que todos sus componentes se muestren correctamente.
        setLocationRelativeTo(mainForm); // Centra la ventana del diálogo relative a la ventana principal.

        // Agrega un ActionListener al botón 'btnCancel' para cerrar la ventana actual (UserWriteForm).
        btnCancel.addActionListener(s -> this.dispose());
        // Agrega an ActionListener to the 'btnOk' to trigger the save/update/delete action
        btnOk.addActionListener(s -> ok());
    }

    private void init() {
        // Inicializa el ComboBox de estatus (cbEstatus) con las opciones correspondientes.
        initCBStatus();
//        initCBNivelUsuario();

        // Realiza acciones específicas en la interfaz de usuario basadas en el tipo de operación (CUD).
        switch (this.cud) {
            case CREATE:
                // Si la operación es de creación, establece el título de la ventana como "Crear Usuario".
                setTitle("Crear Usuario");
                // Establece el texto del botón de acción principal (btnOk) como "Guardar".
                btnOk.setText("Guardar");
                break;
            case UPDATE:
                // Si la operación es de actualización, establece el título de la ventana como "Modificar Usuario".
                setTitle("Modificar Usuario");
                // Establece el texto del botón de acción principal (btnOk) como "Guardar".
                btnOk.setText("Guardar");
                break;
            case DELETE:
                // Si la operación es de eliminación, establece el título de la ventana como "Eliminar Usuario".
                setTitle("Eliminar Usuario");
                // Establece el texto del botón de acción principal (btnOk) como "Eliminar".
                btnOk.setText("Eliminar");
                break;
        }

        // Llama al método 'setValuesControls' para llenar los campos del formulario
        // con los valores del objeto User proporcionado ('this.en').
        // Esto es especialmente útil para las operaciones de actualización y eliminación,
        // donde se deben mostrar los datos existentes del usuario.
        setValuesControls(this.en);
    }

//    private void initCBNivelUsuario() {
//        DefaultComboBoxModel<CBOption> model = new DefaultComboBoxModel<>();
//        cbNivelUsuario.setModel(model); // <-- Aquí se asigna explícitamente el modelo
//        try {
//            NivelUsuarioDAO nivelUsuarioDAO = new NivelUsuarioDAO();
//            ArrayList<NivelUsuario> niveles = nivelUsuarioDAO.search("");
//
//            for (NivelUsuario nivel : niveles) {
//                model.addElement(new CBOption(nivel.getName(), nivel.getIdNivel()));
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Error al cargar niveles de usuario: " + ex.getMessage());
//        }
//    }


    private void initCBStatus() {
        // Obtiene el modelo actual del ComboBox 'cbStatus' y lo castea a DefaultComboBoxModel
        // para poder manipular sus elementos.
        DefaultComboBoxModel<CBOption> model = (DefaultComboBoxModel<CBOption>) cbStatus.getModel();

        // Crea una nueva opción 'ACTIVO' con un valor asociado de byte 1 y la agrega al modelo del ComboBox.
        // Cuando esta opción se seleccione en el ComboBox, el valor subyacente será (byte)1.
        model.addElement(new CBOption("ACTIVO", (byte)1));

        // Crea una nueva opción 'INACTIVO' con un valor asociado de byte 2 y la agrega al modelo del ComboBox.
        // Cuando esta opción se seleccione en el ComboBox, el valor subyacente será (byte)2.
        model.addElement(new CBOption("INACTIVO", (byte)2));
    }

    private void setValuesControls(Usuario user) {
        // Llena el campo de texto 'txtName' con el nombre del usuario.
        txtName.setText(user.getName());

        // Llena el campo de texto 'txtEmail' con el correo electrónico del usuario.
        txtEmail.setText(user.getEmail());

        // Seleccionar el estatus en el ComboBox 'cbStatus'.
        cbStatus.setSelectedItem(new CBOption(null, user.getStatus()));

        txtNivelUsuario.setText(String.valueOf(user.getIdNivelUsuario()));

//        cbNivelUsuario.setSelectedItem(new CBOption(null, user.getIdNivelUsuario()));


        // Si la operación actual es la creación de un nuevo usuario (CUD.CREATE).
        if (this.cud == CUD.CREATE) {
            // Establece el estatus seleccionado en 'cbStatus' como 'Activo'.
            cbStatus.setSelectedItem(new CBOption(null, 1));
        }

        // Si la operación actual es la eliminación de un usuario (CUD.DELETE).
        if (this.cud == CUD.DELETE) {
            // Deshabilita la edición del campo de texto 'txtName' para evitar modificaciones.
            txtName.setEditable(false);
            // Deshabilita la edición del campo de texto 'txtEmail' para evitar modificaciones.
            txtEmail.setEditable(false);
            // Deshabilita el ComboBox 'cbStatus' para evitar cambios en el estatus.
            cbStatus.setEnabled(false);
            // Deshabilita la edición del campo de texto 'txtNivelUsuario' para evitar modificaciones.
            txtNivelUsuario.setEditable(false);

        }

        // Si la operación actual no es la creación de un usuario (es decir, es actualización o eliminación).
        if (this.cud != CUD.CREATE) {
            // Oculta el campo de contraseña 'txtPassword'.
            txtPassword.setVisible(false);
            // Oculta la etiqueta de la contraseña 'lbPassword'.
            lbPassword.setVisible(false);
        }
    }

    private boolean getValuesControls() {
        boolean res = false; // Inicializa la variable 'res' a false (indicando inicialmente que la validación falla).

        // Obtiene la opción seleccionada del ComboBox 'cbStatus'.
        CBOption selectedOption = (CBOption) cbStatus.getSelectedItem();
        // Obtiene el valor del estatus de la opción seleccionada.
        // Si no hay ninguna opción seleccionada (selectedOption es null), se asigna el valor 0 al estatus.
        byte status = selectedOption != null ? (byte) (selectedOption.getValue()) : (byte) 0;

        // Realiza una serie de validaciones en los campos de entrada:

        // 1. Verifica si el campo de texto 'txtName' está vacío (después de eliminar espacios en blanco al inicio y al final).
        if (txtName.getText().trim().isEmpty()) {
            return res; // Si está vacío, retorna false (validación fallida).
        }
        // 2. Verifica si el campo de texto 'txtEmail' está vacío (después de eliminar espacios en blanco al inicio y al final).
        else if (txtEmail.getText().trim().isEmpty()) {
            return res; // Si está vacío, retorna false (validación fallida).
        }
        // 3. Verifica si el estatus es igual a 0.
        // (Asume que 0 es un valor inválido o no seleccionado para el estatus).
        else if (status == (byte) 0) {
            return res; // Si es 0, retorna false (validación fallida).
        }

        // 4. Verifica si la operación actual no es la creación (CUD.CREATE)
        // Y si el ID del objeto User 'en' es 0.
        // Esto podría indicar un error o inconsistencia en los datos para la actualización o eliminación.
        else if (this.cud != CUD.CREATE && this.en.getIdUsuario() == 0) {
            return res; // Si se cumple la condición, retorna false (validación fallida).
        }
// 5. Validar que se haya ingresado un IdNivelUsuario válido
        String nivelStr = txtNivelUsuario.getText().trim();
        if (nivelStr.isEmpty()) {
            return false; // Campo obligatorio
        }

        int idNivel;
        try {
            idNivel = Integer.parseInt(nivelStr);
            if (idNivel <= 0) return false;
        } catch (NumberFormatException e) {
            return false; // Valor no numérico
        }

// Asignar el valor al objeto Usuario
        this.en.setIdNivelUsuario(idNivel);

        // Si todas las validaciones anteriores pasan, se considera que los datos son válidos.
        res = true; // Establece 'res' a true.

        // Actualiza los atributos del objeto User 'en' con los valores ingresados en los campos:

        // Establece el nombre del usuario.
        this.en.setName(txtName.getText());
        // Establece el correo electrónico del usuario.
        this.en.setEmail(txtEmail.getText());
        // Establece el estatus del usuario.
        this.en.setStatus(status);

        // Si la operación actual es la creación (CUD.CREATE),
        // Establece la contraseña del usuario.
        // Se obtiene la contraseña del campo 'txtPassword' como un array de caracteres y se convierte a String.
        if (this.cud == CUD.CREATE) {
            this.en.setPasswordHash(new String(txtPassword.getPassword()));
            if (this.en.getPasswordHash().trim().isEmpty()){
                return false;
            }
        }
//        // Obtener el valor seleccionado del ComboBox cbNivelUsuario
//        CBOption selectedNivel = (CBOption) cbNivelUsuario.getSelectedItem();
//        int idNivelSeleccionado = selectedNivel != null ? (int) selectedNivel.getValue() : 0;

// Validar que se haya seleccionado un nivel válido
//        if (idNivelSeleccionado == 0) {
//            return false;
//        }
//
//// Asignar el nivel al objeto usuario
//        this.en.setIdNivelUsuario(idNivelSeleccionado);
        // Retorna true, indicando que los datos son válidos y se han asignado al objeto User.
        return res;
    }

    private void ok() {
        try {
            // Obtener y validar los valores de los controles del formulario.
            boolean res = getValuesControls();

            // Si la validación de los controles fue exitosa.
            if (res) {
                boolean r = false; // Variable para almacenar el resultado de la operación de la base de datos.

                // Realiza la operación de la base de datos según el tipo de operación actual (CREATE, UPDATE, DELETE).
                switch (this.cud) {
                    case CREATE:
                        // Caso de creación de un nuevo usuario.
                        // Llama al método 'create' de userDAO para persistir el nuevo usuario (this.en).
                        Usuario user = userDAO.create(this.en);
                        // Verifica si la creación fue exitosa comprobando si el nuevo usuario tiene un ID asignado.
                        if (user.getIdUsuario() > 0) {
                            r = true; // Establece 'r' a true si la creación fue exitosa.
                        }
                        break;
                    case UPDATE:
                        // Caso de actualización de un usuario existente.
                        // Llama al método 'update' de userDAO para guardar los cambios del usuario (this.en).
                        r = userDAO.update(this.en); // 'r' será true si la actualización fue exitosa, false en caso contrario.
                        break;
                    case DELETE:
                        // Caso de eliminación de un usuario.
                        // Llama al método 'delete' de userDAO para eliminar el usuario (this.en).
                        r = userDAO.delete(this.en); // 'r' será true si la eliminación fue exitosa, false en caso contrario.
                        break;
                }

                // Si la operación de la base de datos (creación, actualización o eliminación) fue exitosa.
                if (r) {
                    // Muestra un mensaje de éxito al usuario.
                    JOptionPane.showMessageDialog(null,
                            "Transacción realizada exitosamente",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    // Cierra la ventana actual (UserWriteForm).
                    this.dispose();
                } else {
                    // Si la operación de la base de datos falló.
                    JOptionPane.showMessageDialog(null,
                            "No se logró realizar ninguna acción",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    return; // Sale del método.
                }
            } else {
                // Si la validación de los controles falló (algún campo obligatorio está vacío o inválido).
                JOptionPane.showMessageDialog(null,
                        "Los campos con * son obligatorios",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return; // Sale del método.
            }
        } catch (Exception ex) {
            // Captura cualquier excepción que ocurra durante el proceso (por ejemplo, errores de base de datos).
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            return; // Sale del método.
        }
    }

}