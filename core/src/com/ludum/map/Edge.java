package com.ludum.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.ludum.physics.PhysicsDataStructure;
import com.ludum.physics.PhysicsManager;
import com.ludum.physics.PhysicsObject;
import com.ludum.physics.PhysicsObjectType;

public class Edge implements PhysicsObject {

	private Body body;

	public Edge(Vector2 beg, Vector2 end, WorldType type) {
		PhysicsDataStructure pds;
		if (type == WorldType.LIGHT)
			pds = new PhysicsDataStructure(this, PhysicsObjectType.LIGHTEDGE);
		else
			pds = new PhysicsDataStructure(this, PhysicsObjectType.DARKEDGE);
		body = PhysicsManager.getInstance().createEdge(beg, end, pds);
	}

	@Override
	public void BeginContactHandler(PhysicsDataStructure struct, Contact contact) {
		// :D
	}

	@Override
	public void EndContactHandler(PhysicsDataStructure struct, Contact contact) {
		// :D
	}

	@Override
	public void PreContactHandler(PhysicsDataStructure b, Contact contact) {
		// :D
		
	}

}
