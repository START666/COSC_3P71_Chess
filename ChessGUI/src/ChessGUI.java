import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;

@SuppressWarnings("serial")
public class ChessGUI extends GLJPanel implements GLEventListener {

    private static int width;
    private static int height;
    private FPSAnimator animator;
    private float r = 0.0f;

    private GLModel chairModel = null;

    public ChessGUI() {
        setFocusable(true);
        addGLEventListener(this);
        animator = new FPSAnimator(this, 60, false);
        animator.start();
        width = height = 800;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glLoadIdentity();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glTranslatef(0,0,-1);
        gl.glScalef(0.01f, 0.01f, 0.01f);
        gl.glRotatef(r,0.0f,1.0f,0.0f);
        chairModel.opengldraw(gl);

        gl.glFlush();
        r -=0.8f;
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glEnable(GL2.GL_NORMALIZE);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU glu = new GLU();

        if (false == loadModels(gl)) {
            System.exit(1);
        }

        setLight(gl);

        glu.gluPerspective(2, (double) getWidth() / getHeight(), 0.1, 10);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void setLight(GL2 gl) {

        gl.glEnable(GL2.GL_LIGHTING);

        float SHINE_ALL_DIRECTIONS = 1;
        float[] lightPos = { -30, 30, 30, SHINE_ALL_DIRECTIONS };
        float[] lightColorAmbient = { 0.02f, 0.02f, 0.02f, 1f };
        float[] lightColorSpecular = { 0.9f, 0.9f, 0.9f, 1f };

        // Set light parameters.
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightColorSpecular, 0);
        gl.glEnable(GL2.GL_LIGHT1);

    }

    private Boolean loadModels(GL2 gl) {
        chairModel = ObjLoader.LoadModel("ChessGUI/models/monkey.obj","ChessGUI/models/c.mtl", gl);
        if (chairModel == null) {
            return false;
        }
        return true;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                        int height) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();

        glu.gluPerspective(2, (double) getWidth() / getHeight(), 0.1, 10);
        gl.glMatrixMode(GL2.GL_MODELVIEW);

    }

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.getContentPane().add(new ChessGUI());
        window.setSize(width, height);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
