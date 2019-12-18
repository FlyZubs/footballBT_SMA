package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EMatchState;

public class IsFreeKick extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if (agent.getMatchPerception().getState() == EMatchState.FREE_KICK_LEFT
				|| agent.getMatchPerception().getState() == EMatchState.FREE_KICK_RIGHT)
			return BTStatus.SUCCESS;
		return BTStatus.FAILURE;
	}

}
