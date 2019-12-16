package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EMatchState;

public class IsKickOff extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if (agent.getMatchPerception().getState() == (EMatchState.BEFORE_KICK_OFF)
				|| agent.getMatchPerception().getState() == (EMatchState.KICK_OFF_LEFT)
				|| agent.getMatchPerception().getState() == (EMatchState.KICK_OFF_RIGHT)) {
			return BTStatus.SUCCESS;
		} else {
			return BTStatus.FAILURE;
		}
	}

}
