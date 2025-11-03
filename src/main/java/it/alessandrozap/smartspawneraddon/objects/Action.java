package it.alessandrozap.smartspawneraddon.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Action {
    private boolean enabled;
    private String color;
    private List<String> description;
}