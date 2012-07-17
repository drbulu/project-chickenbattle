
import java.io.IOException;

import network.GameClient;
import network.Packet.BlockUpdate;
import network.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


public class Application implements InputProcessor{
	public Renderer renderer;
	public Cube cube;
	public Boolean send;
	PerspectiveCamera cam;
	int draggedX;
	int draggedY;
	float angleUP, angleLeft;
	public Map map;
	public LightSource light;
	public Vector3 from;
	public Vector3 to;
	public Vector3 oldPos;
	public Character ch;
	public Array<BlockUpdate> chunkstoupdate;
	public Array<Chunk> chunkstorebuild;
	Vector3 movement;
	GameClient client;
	public Player[] players;
	float forceUp;
	boolean jumping;
	int timer;
	boolean multiplayer;
	int mptimer;
	public Application(){
		movement = new Vector3();
		ch = new Character();
		ch.setPos(0,50,40);
		oldPos = new Vector3();
		from = new Vector3(0,0,0);
		to = new Vector3(0,0,0);
		light = new LightSource(-100,200,0);
		chunkstoupdate = new Array<BlockUpdate>();
		chunkstorebuild = new Array<Chunk>();
		map = new Map();
		send = false;
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0,50,40);
		cam.update();
		renderer = new Renderer();

		multiplayer = false;
		if(multiplayer){
			players = new Player[10];
			client = new GameClient();
			System.out.println("Client created");
		}
		Gdx.input.setInputProcessor(this);
	}
	public void render() {
		renderer.render(this);
	}
	public void update() {
		Gdx.input.setCursorCatched(true);
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			oldPos.set(ch.position);
			movement.set(cam.direction.x,0,cam.direction.z);
			movement.nor();
			movement.mul(Gdx.graphics.getDeltaTime()*10);
			ch.addMovement(movement);

			for (Vector3 vec : ch.box.getCorners()) {
				int pointX = (int) vec.x;
				int pointY = (int) vec.y;
				int pointZ = (int) vec.z;
				if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
					for (Chunk c : map.chunks) {
						if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
							if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] == 1) {
								ch.setPos(oldPos);
							}		
							break;
						}
					}
				}
			}
			cam.update();
		}
		if (Gdx.input.isKeyPressed((Input.Keys.S))) {
			oldPos.set(ch.position);
			movement.set(cam.direction.x,0,cam.direction.z);
			movement.nor();
			movement.mul(Gdx.graphics.getDeltaTime()*10*-1);
			ch.addMovement(movement);
			for (Vector3 vec : ch.box.getCorners()) {
				int pointX = (int) vec.x;
				int pointY = (int) vec.y;
				int pointZ = (int) vec.z;
				if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
					for (Chunk c : map.chunks) {
						if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
							if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] == 1) {
								ch.setPos(oldPos);
							}		
							break;
						}
					}
				}
			}
			cam.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			oldPos.set(ch.position);
			movement.set(cam.direction.x,0,cam.direction.z);
			movement.crs(cam.up);
			movement.nor();
			movement.mul(Gdx.graphics.getDeltaTime()*10);
			ch.addMovement(movement);
			for (Vector3 vec : ch.box.getCorners()) {
				int pointX = (int) vec.x;
				int pointY = (int) vec.y;
				int pointZ = (int) vec.z;
				if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
					for (Chunk c : map.chunks) {
						if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
							if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] == 1) {
								ch.setPos(oldPos);
							}		
							break;
						}
					}
				}
			}
			cam.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			oldPos.set(ch.position);
			movement.set(cam.direction.x,0,cam.direction.z);
			movement.crs(cam.up);
			movement.nor();
			movement.mul(Gdx.graphics.getDeltaTime()*-10);
			ch.addMovement(movement);
			for (Vector3 vec : ch.box.getCorners()) {
				int pointX = (int) vec.x;
				int pointY = (int) vec.y;
				int pointZ = (int) vec.z;
				if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
					for (Chunk c : map.chunks) {
						if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
							if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] == 1) {
								ch.setPos(oldPos);
							}		
							break;
						}
					}
				}
			}
			cam.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			oldPos.set(ch.position);
			movement.set(0,-1*Gdx.graphics.getDeltaTime()*10,0);
			ch.addMovement(movement);
			for (Vector3 vec : ch.box.getCorners()) {
				int pointX = (int) vec.x;
				int pointY = (int) vec.y;
				int pointZ = (int) vec.z;
				if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
					for (Chunk c : map.chunks) {
						if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
							if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] == 1) {
								jumping = true;
								forceUp = 5;
							}		
							break;
						}
					}
				}
			}
			ch.position.set(oldPos);
		}
		if (jumping) {
			oldPos.set(ch.position);
			movement.set(0,Gdx.graphics.getDeltaTime()*10*forceUp,0);
			ch.addMovement(movement);
			for (Vector3 vec : ch.box.getCorners()) {
				int pointX = (int) vec.x;
				int pointY = (int) vec.y;
				int pointZ = (int) vec.z;
				if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
					for (Chunk c : map.chunks) {
						if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
							if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] == 1) {
								ch.setPos(oldPos);
								jumping = false;
							}		
							break;
						}
					}
				}
				forceUp -= 2.5f*Gdx.graphics.getDeltaTime();
				if (forceUp < 0) {
					jumping = false;
					forceUp = 0;
				}
			}
			cam.update();
		} else {
			oldPos.set(ch.position);
			movement.set(0,Gdx.graphics.getDeltaTime()*10*forceUp,0);
			forceUp -= 2.5f*Gdx.graphics.getDeltaTime();
			ch.addMovement(movement);
			for (Vector3 vec : ch.box.getCorners()) {
				int pointX = (int) vec.x;
				int pointY = (int) vec.y;
				int pointZ = (int) vec.z;
				if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
					for (Chunk c : map.chunks) {
						if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
							if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] == 1) {
								ch.setPos(oldPos);
								forceUp = 0;
							}		
							break;
						}
					}
				}
			}
		}

		//oldPos.set(cam.direction);
		//oldPos.nor();
		//oldPos.mul(5);
		movement.set(ch.position);
		//movement.sub(oldPos);
		movement.add(0,2,0);
		cam.position.set(movement);
		cam.update();
		mptimer+= Gdx.graphics.getDeltaTime()*1000;
		if(mptimer > 35){
			mptimer = 0;
			send = true;
		}

		if (ch.weapon == Weapon.ak && Gdx.input.isTouched()) {
			timer+= Gdx.graphics.getDeltaTime()*1000;
			if (timer > 50) {
				timer = 0;
				touchDown(draggedX,draggedY,0,0);
			}
		}

		if(multiplayer){
			players = client.getPlayers();
			if(players[client.id] != null){	

				players[client.id].posX = ch.position.x; 
				players[client.id].posY = ch.position.y; 
				players[client.id].posZ = ch.position.z; 

				//			System.out.println(players[client.id].posX);
				if(send){
					client.sendMessage(players[client.id]);
					send = false;
				}
			}
			chunkstoupdate.clear();
			chunkstorebuild.clear();
			chunkstoupdate = client.getChunks();
			for(int i=0; i < chunkstoupdate.size; i++ ){
				Chunk c = map.chunks.get(chunkstoupdate.get(i).chunk);
				c.map[chunkstoupdate.get(i).x-c.x*chunkstoupdate.get(i).size][chunkstoupdate.get(i).y-c.y*chunkstoupdate.get(i).size][chunkstoupdate.get(i).z-c.z*chunkstoupdate.get(i).size] = chunkstoupdate.get(i).modi;	
				chunkstorebuild.add(c);
			}

			for(int i=0; i<chunkstorebuild.size; i++){
				chunkstorebuild.get(i).rebuildChunk();
			}
		}
	}
	@Override
	public boolean keyDown(int arg0) {
		if (Input.Keys.NUM_1 == arg0) {
			ch.weapon = ch.inventory.get(0).weaponID;
		} else if (Input.Keys.NUM_2 == arg0) {
			ch.weapon = ch.inventory.get(1).weaponID;
		} else if (Input.Keys.NUM_3 == arg0) {
			ch.weapon = ch.inventory.get(2).weaponID;
		}
		return false;
	}
	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean scrolled(int arg0) {

		return false;
	}
	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		float range = 0;
		Vector3 point = new Vector3(cam.getPickRay(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2).origin);
		Vector3 direction = new Vector3(cam.getPickRay(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2).direction);
		direction.nor();
		direction.mul(0.5f);
		from.set(point);
		to.set(direction).mul(100);
		boolean hit = false;
		int pointX = (int) point.x;
		int pointY = (int) point.y;
		int pointZ = (int) point.z;
		while (!hit && range < 200) {
			range += direction.len();
			point.add(direction);
			pointX = (int) point.x;
			pointY = (int) point.y;
			pointZ = (int) point.z;


			if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
				for (Chunk c : map.chunks) {
					if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
						if (c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] == 1) {
							hit = true;
							System.out.println("hit" + pointX + " " + pointY + " " + pointZ);
						}		
						break;
					}
				}
			}
			if (hit) {
				if (ch.weapon == Weapon.block) {
					point.sub(direction);
					pointX = (int) point.x;
					pointY = (int) point.y;
					pointZ = (int) point.z;
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
						for (Chunk c : map.chunks) {
							if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
								c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] = 1;
								c.rebuildChunk();
								break;
							}		
						}
					}
				} else {
					if (pointX >= 0 && pointX < Map.x && pointY >= 0 && pointY < Map.y && pointZ >= 0 && pointZ < Map.z) {
						System.out.println("in map");

						for (int i = 0; i < map.chunks.size; i++){
							Chunk c = map.chunks.get(i);
							if (c.x == (pointX/Map.chunkSize) && c.y == (pointY/Map.chunkSize) && c.z == (pointZ/Map.chunkSize)) {
								System.out.println("HIT");
								if(multiplayer){
									client.sendChunkUpdate(i, pointX, pointY, pointZ, Map.chunkSize, 0);
									System.out.println("mprevmoce");
								}
								else{
									c.map[pointX-c.x*Map.chunkSize][pointY-c.y*Map.chunkSize][pointZ-c.z*Map.chunkSize] = 0;
									c.rebuildChunk();
									break;
								}
							}

						}
					}
				}
			}
		}
		return false;
	}
	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		touchMoved(arg0,arg1);
		return false;
	}
	@Override
	public boolean touchMoved(int arg0, int arg1) {
		if (draggedX == 0)
			draggedX = arg0;
		if (draggedY == 0)
			draggedY = arg1;
		float deltaX = (arg0-draggedX)*2;
		float deltaY = (arg1-draggedY)*2;
		deltaY = deltaY*-1;
		draggedX = arg0;
		draggedY = arg1;
		angleLeft -= deltaX/5;
		angleUP += deltaY/5;
		if (angleUP < -90)
			angleUP = -90;
		if (angleUP > 90)
			angleUP = 90;
		cam.direction.set(0,0,-1);
		cam.up.set(0,1,0);
		cam.rotate(angleUP, 1, 0, 0);
		cam.rotate(angleLeft, 0, 1, 0);
		cam.update();
		return false;
	}
	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}