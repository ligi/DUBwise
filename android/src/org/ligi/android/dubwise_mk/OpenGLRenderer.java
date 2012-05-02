/**************************************************************************
 *                                          
 * Renderer to render the 3D view of the UAV
 *                                          
 * Author:  Marcus -LiGi- Bueschleb   
 *
 * Project URL:
 *  http://mikrokopter.de/ucwiki/en/DUBwise
 * 
 * License:
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent manner! 
 *  This explicitly includes that lethal Weapon owning "People" and 
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise_mk;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

public class OpenGLRenderer implements Renderer {
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.
         * microedition.khronos.opengles.GL10, javax.microedition.khronos.
         * egl.EGLConfig)
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);  // OpenGL docs.
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);// OpenGL docs.
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);// OpenGL docs.
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);// OpenGL docs.
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);// OpenGL docs.
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, // OpenGL docs.
                          GL10.GL_NICEST);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.
         * microedition.khronos.opengles.GL10)
	 */
	public void onDrawFrame(GL10 gl) {
			// Clears the screen and depth buffer.
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | // OpenGL docs.
                           GL10.GL_DEPTH_BUFFER_BIT);
		
			float nick=0.0f;
			float roll=0.0f;
			float gier=0.0f;
			
		    /*latitude =  (double) FDebug->WinkelGier - GierOffset->Position;
		    longitude = (double) FDebug->WinkelNick;
		    twist = (double)FDebug->WinkelRoll;
*/
			
/*			
		    float matrix[] = {1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

		    gl.glLoadIdentity ();

		    glGetDoublev (GL10.GL_MO .GL_MODELVIEW_MATRIX, matrix);
		    //gl.glr
		    
		    gl.glLoadIdentity ( );
		    
		    float xr = matrix[4];float  yr = matrix[5];float  zr = matrix[6];
		    gl.glRotatef (nick , xr, yr, zr);
		    glMultMatrixd (matrix);
		    gl.glMultMatrixf(m);
		    glGetDoublev (GL_MODELVIEW_MATRIX, matrix);

		    gl.glLoadIdentity ();
		    xr = matrix[8]; yr = matrix[9]; zr = matrix[10];
		    glRotatef (twist , xr, yr, zr);
		    glMultMatrixd (matrix);
		    glGetDoublev (GL_MODELVIEW_MATRIX, matrix);

		    gl.glLoadIdentity ();
		    xr = matrix[0]; yr = matrix[1]; zr = matrix[2];
		    glRotatef (longitude , xr, yr, zr);
		    glMultMatrixd (matrix);
		    glGetDoublev (GL_MODELVIEW_MATRIX, matrix);

		    gl.glLoadIdentity ();
		    glTranslated(0.0, 0.0, -radius);
		    glMultMatrixd (matrix);      // Apply object space orientation matrix.



		//  glColor3f( 0.7, 0.0, 0.0 );      glCallList(CONE);
		  glColor3f( 0.3, 0.3, 0.3 );      glCallList(GLOBE);
		  glCallList(CYLINDER);
		  glColor3f( 0.9, 0.0, 0.0 );      glCallList(VORNE);
		  glCallList(MOTOREN);


		    SwapBuffers(ghDC);

		    Yield();
*/
	
//		gl.gls

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.
         * microedition.khronos.opengles.GL10, int, int)
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);// OpenGL docs.
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);// OpenGL docs.
		// Reset the projection matrix
		gl.glLoadIdentity();// OpenGL docs.
		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f,
                                   (float) width / (float) height,
                                   0.1f, 100.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);// OpenGL docs.
		// Reset the modelview matrix
		gl.glLoadIdentity();// OpenGL docs.
	
	
			
	}
}
