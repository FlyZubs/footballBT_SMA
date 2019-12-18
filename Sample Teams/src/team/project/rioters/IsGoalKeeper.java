package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;

public class IsGoalKeeper extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if (agent.selfPerc.getUniformNumber() == 8)
			return BTStatus.SUCCESS;
		return BTStatus.FAILURE;
	}

}
