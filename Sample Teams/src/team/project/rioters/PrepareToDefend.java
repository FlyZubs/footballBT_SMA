package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;

public class PrepareToDefend extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		Vector2D goTo = new Vector2D(agent.getHomePosition().getX(), agent.getBallPos().getY());
		
		if (agent.isPointsAreClose(agent.getPosition(), goTo, 1.0d)) {
			return BTStatus.SUCCESS;
		}
		
		agent.dash(goTo, 90);
		
		return BTStatus.RUNNING;
	}

}
