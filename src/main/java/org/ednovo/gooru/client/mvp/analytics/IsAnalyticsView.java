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
package org.ednovo.gooru.client.mvp.analytics;
import java.util.ArrayList;

import org.ednovo.gooru.client.gin.IsViewWithHandlers;
import org.ednovo.gooru.shared.model.analytics.GradeJsonData;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;


public interface IsAnalyticsView extends IsViewWithHandlers<AnalyticsUiHandlers>{
    /**
     * This method is used to reset data.
     */
    void resetData();
    /**
     * This method is used to set collection title to the dropdown.
     * @param gradeData
     */
    void setGradeCollectionData(ArrayList<GradeJsonData> gradeData);
    /**
     * This will return the collection summary panel
     * @return
     */
    HTMLPanel getCollectionSummarySlot();
    /**
     * This will return the collection progress panel
     * @return
     */
    HTMLPanel getCollectionProgressSlot();
    /**
     * This method is used to enable the no data message text.
     */
    void setNoDataText();
    /**
     * This method is used to reset the data.
     */
    void resetDataText() ;
	Frame getFrame();
}
