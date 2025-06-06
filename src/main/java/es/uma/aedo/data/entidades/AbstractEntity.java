package es.uma.aedo.data.entidades;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Version
    private Integer version;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractEntity that)) {
            return false; // null or not an AbstractEntity class
        }
        if (getId() != null) {
            return getId().equals(that.getId());
        }
        return super.equals(that);
    }
}
