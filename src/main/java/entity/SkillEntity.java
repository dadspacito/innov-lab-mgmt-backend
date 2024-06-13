package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;
   // Attributes: id, name, type (enum: knowledge, software, hardware, tools)




public class SkillEntity
{
    private long id;
    private String name;
    private String type;

    public SkillEntity()
    {
    }

    public SkillEntity(long id, String name, String type)
    {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
    
}
