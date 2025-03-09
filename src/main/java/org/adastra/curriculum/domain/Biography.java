package org.adastra.curriculum.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Biography.
 */
@Entity
@Table(name = "biography")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Biography implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @NotNull
    @Size(max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Size(max = 50)
    @Column(name = "title", length = 50)
    private String title;

    @NotNull
    @Size(max = 50)
    @Column(name = "phone", length = 50, nullable = false)
    private String phone;

    @NotNull
    @Size(max = 50)
    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @NotNull
    @Size(max = 50)
    @Column(name = "street", length = 50, nullable = false)
    private String street;

    @NotNull
    @Size(max = 50)
    @Column(name = "city", length = 50, nullable = false)
    private String city;

    @NotNull
    @Size(max = 50)
    @Column(name = "country", length = 50, nullable = false)
    private String country;

    @NotNull
    @Size(max = 50)
    @Column(name = "position", length = 50, nullable = false)
    private String position;

    @NotNull
    @Column(name = "employed_from", nullable = false)
    private LocalDate employedFrom;

    @Size(max = 50)
    @Column(name = "image", length = 50)
    private String image;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "biography")
    @JsonIgnoreProperties(value = { "biography" }, allowSetters = true)
    private Set<Education> educations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "biography")
    @JsonIgnoreProperties(value = { "biography" }, allowSetters = true)
    private Set<Language> languages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "biography")
    @JsonIgnoreProperties(value = { "biography" }, allowSetters = true)
    private Set<Skill> skills = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Biography id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Biography firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Biography lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return this.title;
    }

    public Biography title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return this.phone;
    }

    public Biography phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Biography email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return this.street;
    }

    public Biography street(String street) {
        this.setStreet(street);
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public Biography city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public Biography country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPosition() {
        return this.position;
    }

    public Biography position(String position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getEmployedFrom() {
        return this.employedFrom;
    }

    public Biography employedFrom(LocalDate employedFrom) {
        this.setEmployedFrom(employedFrom);
        return this;
    }

    public void setEmployedFrom(LocalDate employedFrom) {
        this.employedFrom = employedFrom;
    }

    public String getImage() {
        return this.image;
    }

    public Biography image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Biography user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Education> getEducations() {
        return this.educations;
    }

    public void setEducations(Set<Education> educations) {
        if (this.educations != null) {
            this.educations.forEach(i -> i.setBiography(null));
        }
        if (educations != null) {
            educations.forEach(i -> i.setBiography(this));
        }
        this.educations = educations;
    }

    public Biography educations(Set<Education> educations) {
        this.setEducations(educations);
        return this;
    }

    public Biography addEducations(Education education) {
        this.educations.add(education);
        education.setBiography(this);
        return this;
    }

    public Biography removeEducations(Education education) {
        this.educations.remove(education);
        education.setBiography(null);
        return this;
    }

    public Set<Language> getLanguages() {
        return this.languages;
    }

    public void setLanguages(Set<Language> languages) {
        if (this.languages != null) {
            this.languages.forEach(i -> i.setBiography(null));
        }
        if (languages != null) {
            languages.forEach(i -> i.setBiography(this));
        }
        this.languages = languages;
    }

    public Biography languages(Set<Language> languages) {
        this.setLanguages(languages);
        return this;
    }

    public Biography addLanguages(Language language) {
        this.languages.add(language);
        language.setBiography(this);
        return this;
    }

    public Biography removeLanguages(Language language) {
        this.languages.remove(language);
        language.setBiography(null);
        return this;
    }

    public Set<Skill> getSkills() {
        return this.skills;
    }

    public void setSkills(Set<Skill> skills) {
        if (this.skills != null) {
            this.skills.forEach(i -> i.setBiography(null));
        }
        if (skills != null) {
            skills.forEach(i -> i.setBiography(this));
        }
        this.skills = skills;
    }

    public Biography skills(Set<Skill> skills) {
        this.setSkills(skills);
        return this;
    }

    public Biography addSkills(Skill skill) {
        this.skills.add(skill);
        skill.setBiography(this);
        return this;
    }

    public Biography removeSkills(Skill skill) {
        this.skills.remove(skill);
        skill.setBiography(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Biography)) {
            return false;
        }
        return getId() != null && getId().equals(((Biography) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Biography{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", title='" + getTitle() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", position='" + getPosition() + "'" +
            ", employedFrom='" + getEmployedFrom() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
