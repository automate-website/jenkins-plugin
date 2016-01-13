package website.automate.plugins.jenkins.model;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractSerializable implements Serializable {

    private static final long serialVersionUID = 3611410129226004606L;
    
    private String id;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractSerializable other = (AbstractSerializable) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
