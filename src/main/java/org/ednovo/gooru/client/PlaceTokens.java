/*******************************************************************************
 * Copyright 2013 Ednovo d/b/a Gooru. All rights reserved.
 * 
 *  http://www.goorulearning.org/
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.ednovo.gooru.client;

import org.ednovo.gooru.client.gin.AppClientFactory;

/**
 * 
 * @fileName : PlaceTokens.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: 06-Dec-2013
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
public interface PlaceTokens {
	String HOME = "home";
	String RESOURCE_SEARCH = "resource-search";
	String COLLECTION_SEARCH = "collection-search";
	String ERROR = "error";
	String SHELF = "mycollections";
	String COLLECTION = "collection";
	String RESOURCE_PLAY_OLD = "resource-play-old";
	String COLLECTION_PLAY = "collection-play";
	String COLLECTION_PLAY_OLD = "collection-play-old";
	String PREVIEW_PLAY = "preview-play";
	String RESOURCE_PLAY="resource-play";
	String TEACH = "no-teach";		//This is not used...
	String FOLDERS = "organizee";
	String EDIT_FOLDERS = "organize-folder";
	String EDIT_CLASSPAGE = "teach";
	String SETTINGS = "settings";
	String CREATEFOLDER = "createfolder";
	String REGISTER="registration";
	String STUDENT = "students-view";
	String STUDY = "myclasses";
	String CLASSHOME = "myclasseshome";
	String PROFILE_PAGE = "profilepage";
	String RUSD_LIBRARY = "rusd";
	
	String DEVICE_NOT_SUPPORTED = "not-supported";
	
	String FTE = "FTE";
	String ONR = "ONR";
	String AUTODESK = "Autodesk";
	String LESSONOPOLY = "Lessonopoly";
	String NGC = "NGC";
	String WSPWH = "WSPWH";
	String PSDPAL = "PSD";
	String FINCAPINC = "NextGenFinance";
	String SAUSD_LIBRARY = "sausd";	
	String DRIVE="drive";
	
	String COMMUNITY = "community";
	String YOUTHVOICES = "youthvoices";
	String GEOEDUCATION = "GeoEducators";
	String LIFEBOARD = "lifeboard";
	String SUSD = "susd";
	String LPS = "lpslibrary";
	String MURRIETA = "murrieta";
	String VALVERDE = "valverde";
	String DISCOVER = "community";
	String CORE_LIBRARY = "CoreLibrary";
	String ESYP = "ESYP";
	String CCST_Cal_TAC = "CCSTCalTAC";
	String LUSD = "lusd";	
	String TICAL = "tical";
	String DASHBOARD ="dashboard";
	String CREATE_ASSIGNMENT="#/list";
	String EDIT_ASSIGNMENT="/assessments#/assessments/";
	String PLAY_ASSIGNMENT="/assessments/";
}