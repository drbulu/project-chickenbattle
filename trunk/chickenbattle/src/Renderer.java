

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;



public class Renderer {
	public Mesh cube;
	Matrix4 cubeModel = new Matrix4();
	Matrix4 cubeModel2 = new Matrix4();
	Matrix4 modelViewProjectionMatrix = new Matrix4();
	Matrix4 modelViewMatrix = new Matrix4();
	Matrix3 normalMatrix = new Matrix3();
	Texture cubeTexture;
	Texture lightTexture;
	ShaderProgram simpleShader;
	ShaderProgram particleShader;
	Sprite crosshair;
	Sprite gun;
	Sprite block;
	SpriteBatch sb;
	//Vector3 frustumVec;
	public Renderer() {
		//frustumVec = new Vector3();
		sb = new SpriteBatch();
		crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairsmaller.png")));
		gun = new Sprite(new Texture(Gdx.files.internal("data/gun.png")));
		block = new Sprite(new Texture(Gdx.files.internal("data/block.png")));
		simpleShader = new ShaderProgram(Gdx.files.internal(
		"data/shaders/simple.vert").readString(), Gdx.files.internal(
		"data/shaders/simple.frag").readString());
		if (!simpleShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile simple shader: "
					+ simpleShader.getLog());

		particleShader = new ShaderProgram(Gdx.files.internal(
		"data/shaders/particleShader.vert").readString(), Gdx.files.internal(
		"data/shaders/particleShader.frag").readString());
		if (!particleShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile shader: "
					+ particleShader.getLog());

		cubeTexture = new Texture(Gdx.files.internal("data/grassmap.png"));
		lightTexture = new Texture(Gdx.files.internal("data/light.png"));
	}
	public void render(Application app) {
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl20.glCullFace(GL20.GL_BACK);
		renderMapChunks(app);
		renderLights(app);
		renderVector(app.from,app.to,app);

		crosshair.setPosition(Gdx.graphics.getWidth()/2-crosshair.getWidth()/2,Gdx.graphics.getHeight()/2-crosshair.getHeight()/2);
		gun.setPosition(Gdx.graphics.getWidth()-gun.getWidth(), 0);
		block.setPosition(Gdx.graphics.getWidth()-block.getWidth(), 0);
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
		if (!app.adding)
			crosshair.setColor(1,0,0,1);
		else
			crosshair.setColor(1,1,1,1);
		sb.begin();
		crosshair.draw(sb);
		if (!app.adding)
			gun.draw(sb);
		else
			block.draw(sb);
		sb.end();
	}
	public void renderMapChunks(Application app) {
		cubeTexture.bind(0);
		simpleShader.begin();
		simpleShader.setUniform4fv("scene_light", app.light.color, 0,4);
		simpleShader.setUniformf("scene_ambient_light", 0.2f,0.2f,0.2f, 1.0f);
		int camInChunkX = (((int)app.cam.position.x)/Map.chunkSize);
		int camInChunkY = (((int)app.cam.position.y)/Map.chunkSize);
		int camInChunkZ = (((int)app.cam.position.z)/Map.chunkSize);
		
		int lowerBoundX = Math.max(0, camInChunkX-3);
		int lowerBoundY = Math.max(0, camInChunkY-3);
		int lowerBoundZ = Math.max(0, camInChunkZ-3);
		
		int upperBoundX = Math.min(Map.x/Map.chunkSize, camInChunkX+3);
		int upperBoundY = Math.min(Map.y/Map.chunkSize, camInChunkY+3);
		int upperBoundZ = Math.min(Map.z/Map.chunkSize, camInChunkZ+3);
		for (int x = lowerBoundX; x < upperBoundX; x++) {
			for (int y = lowerBoundY; y < upperBoundY; y++) {
				for (int z = lowerBoundZ; z < upperBoundZ; z++) {
					//frustumVec.set(x*Map.chunkSize+Map.chunkSize/2,y*Map.chunkSize+Map.chunkSize/2,z*Map.chunkSize+Map.chunkSize/2);
					if (app.map.chunks[x][y][z] != null) {// && app.cam.frustum.sphereInFrustum(frustumVec, Map.chunkSize/2)) {
						simpleShader.setUniformi("s_texture", 0);
						cubeModel.setToTranslation(0,0,0);
						modelViewProjectionMatrix.set(app.cam.combined);
						modelViewProjectionMatrix.mul(cubeModel);
						modelViewMatrix.set(app.cam.view);
						modelViewMatrix.mul(cubeModel);
						normalMatrix.set(modelViewMatrix);
						simpleShader.setUniformMatrix("normalMatrix", normalMatrix);
						simpleShader.setUniformMatrix("u_modelViewMatrix", modelViewMatrix);
						simpleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
						simpleShader.setUniformf("material_diffuse", 1f,1f,1f, 1f);
						simpleShader.setUniformf("material_specular", 0.0f,0.0f,0.0f, 1f);
						simpleShader.setUniformf("material_shininess", 0.5f);
						simpleShader.setUniform3fv("u_lightPos",app.light.getViewSpacePositions(app.cam.view), 0,3);
						app.map.chunks[x][y][z].render(simpleShader, GL20.GL_TRIANGLES);
					}
				}
			}
		}
		simpleShader.end();
	}
	public void renderMap(Application app) {
		cubeTexture.bind(0);
		simpleShader.begin();
		simpleShader.setUniform4fv("scene_light", app.light.color, 0,4);
		simpleShader.setUniformf("scene_ambient_light", 0.2f,0.2f,0.2f, 1.0f);
		for (int x = 0; x < Map.x; x++) {
			for (int y = 0; y < Map.y; y++) {
				for (int z = 0; z < Map.z; z++) {
					if (app.map.map[x][y][z] == 1) {
						simpleShader.setUniformi("s_texture", 0);
						cubeModel.setToTranslation(x,y,z);
						modelViewProjectionMatrix.set(app.cam.combined);
						modelViewProjectionMatrix.mul(cubeModel);
						modelViewMatrix.set(app.cam.view);
						modelViewMatrix.mul(cubeModel);
						normalMatrix.set(modelViewMatrix);
						simpleShader.setUniformMatrix("normalMatrix", normalMatrix);
						simpleShader.setUniformMatrix("u_modelViewMatrix", modelViewMatrix);
						simpleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
						simpleShader.setUniformf("material_diffuse", 1f,1f,1f, 1f);
						simpleShader.setUniformf("material_specular", 0.0f,0.0f,0.0f, 1f);
						simpleShader.setUniformf("material_shininess", 0.5f);
						simpleShader.setUniform3fv("u_lightPos",app.light.getViewSpacePositions(app.cam.view), 0,3);
						Cube.cubeMesh.render(simpleShader, GL20.GL_TRIANGLES);
					}
				}
			}
		}
		simpleShader.end();
	}
	public void renderLights(Application app) {
		lightTexture.bind(0);
		simpleShader.begin();
		simpleShader.setUniformi("s_texture", 0);
		cubeModel.setToTranslation(app.light.posX,app.light.posY,app.light.posZ);
		modelViewProjectionMatrix.set(app.cam.combined);
		modelViewProjectionMatrix.mul(cubeModel);
		simpleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
		app.cube.cubeMesh.render(simpleShader, GL20.GL_TRIANGLES);
		simpleShader.end();
	}

	public void renderVector(Vector3 from,Vector3 to, Application app) {
		Mesh vectorTest = new Mesh(true,2,0,new VertexAttribute(Usage.Position, 3,"a_position"));
		particleShader.begin();
		float[] vertices = new float[]{ from.x,from.y,from.z,to.x,to.y,to.z}; 
		vectorTest.setVertices(vertices);


		Matrix4 modelViewProjectionMatrix = new Matrix4();
		modelViewProjectionMatrix.set(app.cam.combined);

		particleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);

		vectorTest.render(particleShader, GL20.GL_LINES);
		particleShader.end();
		vectorTest.dispose();
	}
}