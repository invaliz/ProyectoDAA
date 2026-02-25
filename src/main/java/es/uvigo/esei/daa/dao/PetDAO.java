package es.uvigo.esei.daa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.daa.entities.Pet;

/**
 * DAO class for the {@link Pet} entities.
 * 
 * @author DRM
 *
 */
public class PetDAO extends DAO {
    private final static Logger LOG = Logger.getLogger(PetDAO.class.getName());
    
    public Pet get(int id) throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM pet WHERE id=?";
            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);
                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return rowToEntity(result);
                    } else {
                        throw new IllegalArgumentException("Invalid id");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error getting a pet", e);
            throw new DAOException(e);
        }
    }
    
    public List<Pet> list() throws DAOException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM pet";
            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                try (final ResultSet result = statement.executeQuery()) {
                    final List<Pet> pets = new LinkedList<>();
                    while (result.next()) {
                        pets.add(rowToEntity(result));
                    }
                    return pets;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error listing pets", e);
            throw new DAOException(e);
        }
    }
    
    public List<Pet> listByPerson(int personId) throws DAOException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM pet WHERE person_id=?";
            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, personId);
                try (final ResultSet result = statement.executeQuery()) {
                    final List<Pet> pets = new LinkedList<>();
                    while (result.next()) {
                        pets.add(rowToEntity(result));
                    }
                    return pets;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error listing pets by person", e);
            throw new DAOException(e);
        }
    }
    
    public Pet add(String name, String breed, int birthYear, int personId) throws DAOException, IllegalArgumentException {
        if (name == null || breed == null) {
            throw new IllegalArgumentException("name and breed can't be null");
        }
        try (Connection conn = this.getConnection()) {
            final String query = "INSERT INTO pet VALUES(null, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, name);
                statement.setString(2, breed);
                statement.setInt(3, birthYear);
                statement.setInt(4, personId);
                if (statement.executeUpdate() == 1) {
                    try (ResultSet resultKeys = statement.getGeneratedKeys()) {
                        if (resultKeys.next()) {
                            return new Pet(resultKeys.getInt(1), name, breed, birthYear, personId);
                        } else {
                            LOG.log(Level.SEVERE, "Error retrieving inserted id");
                            throw new SQLException("Error retrieving inserted id");
                        }
                    }
                } else {
                    LOG.log(Level.SEVERE, "Error inserting value");
                    throw new SQLException("Error inserting value");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error adding a pet", e);
            throw new DAOException(e);
        }
    }
    
    public void modify(Pet pet) throws DAOException, IllegalArgumentException {
        if (pet == null) {
            throw new IllegalArgumentException("pet can't be null");
        }
        try (Connection conn = this.getConnection()) {
            final String query = "UPDATE pet SET name=?, breed=?, birth_year=? WHERE id=?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, pet.getName());
                statement.setString(2, pet.getBreed());
                statement.setInt(3, pet.getBirthYear());
                statement.setInt(4, pet.getId());
                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("Invalid pet id");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error modifying a pet", e);
            throw new DAOException(e);
        }
    }
    
    public void delete(int id) throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "DELETE FROM pet WHERE id=?";
            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);
                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("Invalid id");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error deleting a pet", e);
            throw new DAOException(e);
        }
    }
    
    private Pet rowToEntity(ResultSet row) throws SQLException {
        return new Pet(row.getInt("id"), row.getString("name"), row.getString("breed"), row.getInt("birth_year"), row.getInt("person_id"));
    }
}