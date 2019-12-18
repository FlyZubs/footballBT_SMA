package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.EMatchState;

public class IsMyTeam extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if ((agent.getSide() == EFieldSide.LEFT && (agent.getMatchPerception().getState() == EMatchState.KICK_OFF_LEFT
				|| agent.getMatchPerception().getState() == EMatchState.KICK_IN_LEFT
				|| agent.getMatchPerception().getState() == EMatchState.FREE_KICK_LEFT
				|| agent.getMatchPerception().getState() == EMatchState.CORNER_KICK_LEFT
				|| agent.getMatchPerception().getState() == EMatchState.GOAL_KICK_LEFT
				|| agent.getMatchPerception().getState() == EMatchState.OFFSIDE_LEFT
				|| agent.getMatchPerception().getState() == EMatchState.BACK_PASS_LEFT
				|| agent.getMatchPerception().getState() == EMatchState.FREE_KICK_FAULT_LEFT
				|| agent.getMatchPerception().getState() == EMatchState.INDIRECT_FREE_KICK_LEFT))
				|| (agent.getSide() == EFieldSide.RIGHT
						&& (agent.getMatchPerception().getState() == EMatchState.KICK_OFF_RIGHT
								|| agent.getMatchPerception().getState() == EMatchState.KICK_IN_RIGHT
								|| agent.getMatchPerception().getState() == EMatchState.FREE_KICK_RIGHT
								|| agent.getMatchPerception().getState() == EMatchState.CORNER_KICK_RIGHT
								|| agent.getMatchPerception().getState() == EMatchState.GOAL_KICK_RIGHT
								|| agent.getMatchPerception().getState() == EMatchState.OFFSIDE_RIGHT
								|| agent.getMatchPerception().getState() == EMatchState.BACK_PASS_RIGHT
								|| agent.getMatchPerception().getState() == EMatchState.FREE_KICK_FAULT_RIGHT
								|| agent.getMatchPerception().getState() == EMatchState.INDIRECT_FREE_KICK_RIGHT))) {
			return BTStatus.SUCCESS;
		}

		return BTStatus.FAILURE;
	}

}
