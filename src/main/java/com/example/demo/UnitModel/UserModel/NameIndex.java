package com.example.demo.UnitModel.UserModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@Table(name = "NameIndex")
@NoArgsConstructor
@AllArgsConstructor
public class NameIndex {
    @Id
    @Column(name = "Name")
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "Только латиница, цифры и _")
    @NotNull
    private String name;
    @Column(name = "user_Id")
    @NotNull
    private String id;
}
