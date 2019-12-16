package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;

public class GoToHome extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		Vector2D pos;
		EFieldSide side = agent.selfPerc.getSide();
		
		if (side == EFieldSide.LEFT) {
			pos = agent.getHomePosition();
		} else {
			double x, y;
			x = agent.getHomePosition().getX() * side.value();
			y = agent.getHomePosition().getY() * side.value();
			pos = new Vector2D(x,y);
		}
		agent.dash(pos, 100);
		
		if (agent.isPointsAreClose(agent.selfPerc.getPosition(), agent.getHomePosition(), 1)) {
			return BTStatus.SUCCESS;
		} else {
			return BTStatus.RUNNING;
		}
	}

}
