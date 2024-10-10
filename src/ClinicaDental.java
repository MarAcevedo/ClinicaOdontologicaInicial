
import org.apache.log4j.Logger;

import java.sql.*;

public class ClinicaDental {
    private static final String SQL_CREATE_TABLE = "DROP TABLE IF EXISTS DENTIST; "
            + "CREATE TABLE DENTIST " + "("
            + "ID INT PRIMARY KEY, "
            + "REGISTRATION INT NOT NULL, "
            + "NAME VARCHAR(100) NOT NULL, "
            + "LASTNAME VARCHAR(100) NOT NULL"
            + ")";

    private static final String SQL_INSERT = "INSERT INTO DENTIST(ID, REGISTRATION, NAME, LASTNAME)" +
    " VALUES (?,?,?,?)";

    private static  final String SQL_SELECT = "SELECT * FROM DENTIST";

    private static final String SQL_UPDATE = "UPDATE DENTIST SET NAME =? WHERE ID =?";

    private static final String SQL_SELECT_ID = "SELECT * FROM DENTIST WHERE ID =?";

    private static final String SQL_DELETE = "DELETE FROM DENTIST WHERE ID=?";

    private static final Logger LOGGER = Logger.getLogger(ClinicaDental.class);

    public static void main(String[] args) throws SQLException {

        Dentist dentist1 = new Dentist(1, 5567, "Alicia", "Gonzales");
        Dentist dentist2 = new Dentist(2, 6743, "Marcos", "Garcia");

            Connection connection = null;



            try{
                connection = getConnection();
                Statement statement = connection.createStatement();
                statement.execute(SQL_CREATE_TABLE);

                PreparedStatement psInstert = connection.prepareStatement(SQL_INSERT);
                psInstert.setInt(1, dentist1.getId());
                psInstert.setInt(2, dentist1.getRegistration());
                psInstert.setString(3, dentist1.getName());
                psInstert.setString(4, dentist1.getLastName());

                psInstert.execute();

                psInstert.setInt(1, dentist2.getId());
                psInstert.setInt(2, dentist2.getRegistration());
                psInstert.setString(3, dentist2.getName());
                psInstert.setString(4, dentist2.getLastName());

                psInstert.execute();

                ResultSet rs = statement.executeQuery(SQL_SELECT);

                int n= 1;

                while (rs.next()){

                    System.out.println("Los valores de la fila " + n + " son: " +
                            "\n-ID: " + rs.getInt(1) +
                            "\n-Matricula: "+ rs.getInt(2) +
                            "\n-Nombre: " + rs.getString(3) +
                            "\n-Apellido: " + rs.getString(4));

                    LOGGER.info("Los valores de la fila " + n + " son: " +
                            "\n-ID: " + rs.getInt(1) +
                            "\n-Matricula: "+ rs.getInt(2) +
                            "\n-Nombre: " + rs.getString(3) +
                            "\n-Apellido: " + rs.getString(4));

                    n+=1;
                }

                //Actualizar dato (Transaccion)

                connection.setAutoCommit(false);

                String update = "Vanina";

                PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE);

                psUpdate.setString(1, update);
                psUpdate.setInt(2, dentist1.getId());
                psUpdate.execute();

                connection.commit();
                LOGGER.warn("Se actualizo el registro correspondiente al ID: " + dentist1.getId());
                connection.setAutoCommit(true);


                //Borrar un registro (Transaccion)

                connection.setAutoCommit(false);
                PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE);

                psDelete.setInt(1,dentist2.getId());
                psDelete.execute();

                connection.commit();
                LOGGER.warn("Se elimino el registro con el ID = 2");
                connection.setAutoCommit(true);

                int error = 10/0;

            }catch (Exception e){
                connection.rollback();
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }finally {
                try{
                    connection.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            try{

                connection = getConnection();

                int id = 1;

                PreparedStatement ps2 = connection.prepareStatement(SQL_SELECT_ID);
                ps2.setInt(1,id);

                ResultSet rs = ps2.executeQuery();

                while (rs.next()){
                    System.out.println("-------------------------------------------------------------");

                    System.out.println("Los datos actualizados son: " +
                            "\n-ID: " + rs.getInt(1) +
                            "\n-Matricula: "+ rs.getInt(2) +
                            "\n-Nombre: " + rs.getString(3) +
                            "\n-Apellido: " + rs.getString(4));

                    LOGGER.info("Los datos actualizados son: " +
                            "\n-ID: " + rs.getInt(1) +
                            "\n-Matricula: "+ rs.getInt(2) +
                            "\n-Nombre: " + rs.getString(3) +
                            "\n-Apellido: " + rs.getString(4));

                }

            }catch(Exception e){
                e.printStackTrace();
            }finally {
                try{
                    connection.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        // verificar que se borro el registro de la base de datos

        try{

            connection = getConnection();

            Statement s = connection.createStatement();

            ResultSet rs = s.executeQuery(SQL_SELECT);

            while (rs.next()){

                System.out.println("-------------------------------------------------------------");

                System.out.println("Resultado despues de borrar el id=2 de su base de datos: "+
                        "\n-ID: " + rs.getInt(1) +
                        "\n-Matricula: "+ rs.getInt(2) +
                        "\n-Nombre: " + rs.getString(3) +
                        "\n-Apellido: " + rs.getString(4));

                LOGGER.info("Resultado despues de borrar el id=2 de su base de datos: "+
                        "\n-ID: " + rs.getInt(1) +
                        "\n-Matricula: "+ rs.getInt(2) +
                        "\n-Nombre: " + rs.getString(3) +
                        "\n-Apellido: " + rs.getString(4));

            }


        }catch (Exception e){
            e.printStackTrace();
        }




    }
    private static Connection getConnection() throws Exception{

        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:~/Clinica");

    }

}
