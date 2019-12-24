package core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class SaveState<A extends Enum> implements Serializable {
    private static final long serialVersionUID = 1L;
    private StateActionTable<A> stateActionTable;
    private int currentEpisode;
}
