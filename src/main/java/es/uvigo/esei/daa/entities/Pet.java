package es.uvigo.esei.daa.entities;

import static java.util.Objects.requireNonNull;

/**
 * An entity that represents a pet.
 * 
 * @author DRM
 */
public class Pet {
    private int id;
    private String name;
    private String breed;
    private int birthYear;
    private int personId;
    
    // Constructor needed for the JSON conversion
    Pet() {}
    
    /**
     * Constructs a new instance of {@link Pet}.
     *
     * @param id identifier of the pet.
     * @param name name of the pet.
     * @param breed breed of the pet.
     * @param birthYear birth year of the pet.
     * @param personId identifier of the owner person.
     */
    public Pet(int id, String name, String breed, int birthYear, int personId) {
        this.id = id;
        this.setName(name);
        this.setBreed(breed);
        this.setBirthYear(birthYear);
        this.personId = personId;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = requireNonNull(name, "Name can't be null");
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = requireNonNull(breed, "Breed can't be null");
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Pet))
            return false;
        Pet other = (Pet) obj;
        if (id != other.id)
            return false;
        return true;
    }
}