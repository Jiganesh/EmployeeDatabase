import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employee {
    private JPanel Main;
    private JTextField txtName;
    private JTextField txtSalary;
    private JTextField txtMobile;
    private JButton saveButton;
    private JTable table1;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton searchButton;
    private JTextField txtid;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // Create Database Connection
    // cd Downloads; sudo ./xampp-linux-x64-8.2.0-0-installer.run
    // http://localhost/phpmyadmin/index.php?route=/server/databases

    Connection con;
    PreparedStatement pst;
    public void connect(){
        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/mhacompany", "root", "");
            System.out.println("Success");

        }catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void table_load(){
        try{
            pst = con.prepareStatement("SELECT * FROM employee");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Employee() {

        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            String empname, salary, mobile;

            empname = txtName.getText();
            salary = txtSalary.getText();
            mobile = txtMobile.getText();

            try {
                pst = con.prepareStatement("INSERT INTO employee (empname, salary, mobile) values (?,?,?)");
                pst.setString(1, empname);
                pst.setString(2, salary);
                pst.setString(3, mobile);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Record Added !");
                table_load();
                txtName.setText("");
                txtSalary.setText("");
                txtMobile.setText("");
                txtName.requestFocus();
            }catch (SQLException ex){
                ex.printStackTrace();
            }




           }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String empid = txtid.getText();
                    pst = con.prepareStatement("SELECT empname, salary, mobile FROM employee WHERE id=?" );
                    pst.setString(1, empid);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()==true){

                        String empname = rs.getString(1);
                        String empsalary =  rs.getString(2);
                        String empmobile = rs.getString(3);

                        txtName.setText(empname);
                        txtSalary.setText(empsalary);
                        txtMobile.setText(empmobile);

                    }else{

                        txtName.setText("");
                        txtSalary.setText("");
                        txtMobile.setText("");

                        JOptionPane.showMessageDialog(null, "Invalid Employee ID");
                    }
                }catch(SQLException e1){
                    e1.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empid, empname, salary, mobile;

                empname = txtName.getText();
                salary = txtSalary.getText();
                mobile = txtMobile.getText();
                empid = txtid.getText();


                try{
                    pst = con.prepareStatement("UPDATE employee set empname = ? , salary = ?, mobile = ? where id = ? ");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobile);
                    pst.setString(4, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Updated Record for " + empid);
                    table_load();

                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                    txtid.setText("");

                }catch (SQLException e2){
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    e2.printStackTrace();
                }

            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String empid = txtid.getText();
                    pst = con.prepareStatement("SELECT empname, salary, mobile FROM employee WHERE id=?" );
                    pst.setString(1, empid);
                    ResultSet rs = pst.executeQuery();


                    pst = con.prepareStatement("DELETE FROM employee where id = ?" );
                    pst.setString(1, empid);
                    pst.executeUpdate();


                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");

                    if (rs.next()) JOptionPane.showMessageDialog(null, "Record Deleted");
                    else JOptionPane.showMessageDialog(null, "Record does not exist !");

                    table_load();
                }catch(SQLException e1){
                    e1.printStackTrace();
                }

            }
        });
    }
}

