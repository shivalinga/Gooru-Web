package org.ednovo.gooru.client.mvp.analytics.collectionSummaryIndividual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.analytics.collectionSummary.CollectionSummaryWidget;
import org.ednovo.gooru.client.mvp.analytics.util.AnalyticsReactionWidget;
import org.ednovo.gooru.client.mvp.analytics.util.AnalyticsTabContainer;
import org.ednovo.gooru.client.mvp.analytics.util.AnalyticsUtil;
import org.ednovo.gooru.client.mvp.analytics.util.DataView;
import org.ednovo.gooru.client.mvp.analytics.util.Print;
import org.ednovo.gooru.client.mvp.analytics.util.ViewResponsesPopup;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo;
import org.ednovo.gooru.shared.model.analytics.MetaDataDo;
import org.ednovo.gooru.shared.model.analytics.OetextDataDO;
import org.ednovo.gooru.shared.model.analytics.PrintUserDataDO;
import org.ednovo.gooru.shared.model.analytics.UserDataDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.ajaxloader.client.Properties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Clear;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class CollectionSummaryIndividualView  extends BaseViewWithHandlers<CollectionSummaryIndividualUiHandlers> implements IsCollectionSummaryIndividualView  {

	private static CollectionSummaryIndividualViewUiBinder uiBinder = GWT
			.create(CollectionSummaryIndividualViewUiBinder.class);

	interface CollectionSummaryIndividualViewUiBinder extends
			UiBinder<Widget, CollectionSummaryIndividualView> {
	}
	private static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@UiField HTMLPanel maincontainer,printWidget,totalAvgReactionlbl,tabContainer,individualScoredData,individualOpenendedData,individualScoredDatapnl,individualResourceBreakdownDatapnl,individualResourceBreakdownData;
	@UiField ListBox filterDropDown;
	@UiField Label noErrorMesage,lblCollectionOverview,lblTotalTimeSpent,lblViews,lblAvgReaction,totalTimeSpentlbl,totalViewlbl;
	@UiField Frame downloadFile;
	
	AnalyticsTabContainer individualTabContainer;
	DataView operationsView;
	CollectionSummaryIndividualCBundle res;
	private int collectionProgressCount=1;
	ViewResponsesPopup popupPanel=null;
	
	final List<Integer> questionRowIndex=new ArrayList<Integer>();
	final List<Integer> resourceRowIndex=new ArrayList<Integer>();
	
	final String INCORRECT="#db0f0f",CORRECT="#4d9645",ONMULTIPULEATTEMPTS="#FBB03B";
	final String SCORED="scoredTab",OPENENDED="openendedTab",BREAKDOWN="breakdownTab",PRINT="print",EMAIL="email";
	private static final String QUESTION = "question";
	private static final String VIEWRESPONSE = "View Response";
	
	ArrayList<UserDataDo> questionsData=new ArrayList<UserDataDo>();
	ArrayList<UserDataDo> openendedData=new ArrayList<UserDataDo>();
	EmailPopup emailPopup=null;
	String collectionTitle=null;
	
	//Used for print
	HTMLPanel printScoredData=new HTMLPanel("");
	HTMLPanel printOpendedData=new HTMLPanel("");
	HTMLPanel printResourceData=new HTMLPanel("");
	
	CollectionOverViewWidget collectionOverViewWidget=new CollectionOverViewWidget();
	CollectionSummaryWidget collectionSummaryWidget=new CollectionSummaryWidget();
	
	String style="";

	/**
	 * Constructor
	 */
	public CollectionSummaryIndividualView() {
		this.res = CollectionSummaryIndividualCBundle.INSTANCE;
		res.css().ensureInjected();
		setWidget(uiBinder.createAndBindUi(this));
		String urlDomain=Window.Location.getProtocol()+"//"+Window.Location.getHost();
		style="<link rel='styleSheet' type='text/css' href='"+urlDomain+"/css/googleVisualization.css'><link href='"+urlDomain+"/css/printAnalytics.css' rel='stylesheet' type='text/css'>";
		setData();
		setStaticData();
		noErrorMesage.setVisible(false);
		maincontainer.setVisible(false);
		downloadFile.setVisible(false);
	}
	/**
	 * This method is used to set static data.
	 */
	void setStaticData(){
		StringUtil.setAttributes(printWidget.getElement(), "pnlPrintWidget", null, null);
		StringUtil.setAttributes(totalAvgReactionlbl.getElement(), "pnlTotalAvgReactionlbl", null, null);
		//StringUtil.setAttributes(tabContainer.getElement(), "pnlTabContainer", null, null);
		StringUtil.setAttributes(individualScoredData.getElement(), "pnlIndividualScoredData", null, null);
		StringUtil.setAttributes(individualOpenendedData.getElement(), "pnlIndividualOpenendedData", null, null);
		StringUtil.setAttributes(individualScoredDatapnl.getElement(), "pnlIndividualScoredDatapnl", null, null);
		StringUtil.setAttributes(individualResourceBreakdownDatapnl.getElement(), "pnlIndividualResourceBreakdownDatapnl", null, null);
		StringUtil.setAttributes(individualResourceBreakdownData.getElement(), "pnlIndividualResourceBreakdownData", null, null);

		StringUtil.setAttributes(filterDropDown.getElement(), "ddlFilterDropDown", null, null);

		StringUtil.setAttributes(totalTimeSpentlbl.getElement(), "lblTotalTimeSpentlbl", null, null);
		StringUtil.setAttributes(totalViewlbl.getElement(), "lblTotalViewlbl", null, null);
		StringUtil.setAttributes(lblCollectionOverview.getElement(), "lblCollectionOverview", i18n.GL2274(),  i18n.GL2274());
		StringUtil.setAttributes(lblTotalTimeSpent.getElement(), "lblTotalTimeSpent", i18n.GL2275(),  i18n.GL2275());
		StringUtil.setAttributes(lblViews.getElement(), "lblViews", i18n.GL2276(),  i18n.GL2276());
		StringUtil.setAttributes(lblAvgReaction.getElement(), "lblAvgReaction", i18n.GL2277(),  i18n.GL2277());
	}
	/**
	 * This method is used to hide the panels
	 */
	void hideAllPanels(){
		individualScoredDatapnl.setVisible(false);
		individualOpenendedData.setVisible(false);
		individualResourceBreakdownDatapnl.setVisible(false);
	}
	/**
	 * Default method for initialization
	 */
	void setData(){
		individualTabContainer=new AnalyticsTabContainer() {
			@Override
			public void onTabClick(String tabClicked) {
				if(tabClicked.equalsIgnoreCase(SCORED)){
					hideAllPanels();
					individualScoredDatapnl.setVisible(true);
				}else if(tabClicked.equalsIgnoreCase(OPENENDED)){
					hideAllPanels();
					individualOpenendedData.setVisible(true);
				}else if(tabClicked.equalsIgnoreCase(BREAKDOWN)){
					hideAllPanels();
					individualResourceBreakdownDatapnl.setVisible(true);
				}else if(tabClicked.equalsIgnoreCase(PRINT)){
					setPrintIndividualSummayData(false,false);
				}else if(tabClicked.equalsIgnoreCase(EMAIL)){
					setPrintIndividualSummayData(true,true);
					emailPopup=new EmailPopup();
					emailPopup.setData();
					emailPopup.show();
				}else{
					setPrintIndividualSummayData(true,false);
				}
			}
		};
		tabContainer.add(individualTabContainer);
		filterDropDown.clear();
		filterDropDown.addItem(i18n.GL2289(), i18n.GL2289());
	    filterDropDown.addItem(i18n.GL2290(), i18n.GL2290());
	    filterDropDown.addItem(i18n.GL2291(), i18n.GL2291());
	}
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummaryIndividual.IsCollectionSummaryIndividualView#setIndividualData(java.util.ArrayList, com.google.gwt.user.client.ui.HTMLPanel)
	 */
	@Override
	public void setIndividualData(ArrayList<UserDataDo> result,HTMLPanel loadingImage) {
			noErrorMesage.setVisible(false);
			maincontainer.setVisible(true);
			individualTabContainer.clearStyles();
			individualTabContainer.setScoredQuestionsHilight();
			
			hideAllPanels();
			individualResourceBreakdownDatapnl.setVisible(true);
			
			individualScoredData.clear();
			individualOpenendedData.clear();
			individualResourceBreakdownData.clear();
			questionsData.clear();
			openendedData.clear();

			collectionProgressCount=0;
			questionRowIndex.clear();
			resourceRowIndex.clear();
			Collections.sort(result,new Comparator<UserDataDo>() {
	        	public int compare(UserDataDo o1, UserDataDo o2) {
	        		 Integer obj1 = new Integer(o1.getItemSequence());
					 Integer obj2 = new Integer(o2.getItemSequence());
	        	     return obj1.compareTo(obj2);
	        	}
	        });
			for (UserDataDo userDataDo : result) {
				if(userDataDo.getCategory()!=null && userDataDo.getCategory().equalsIgnoreCase(QUESTION)){
					if(!userDataDo.getType().equalsIgnoreCase("OE")){
						questionsData.add(userDataDo);
					}else{
						openendedData.add(userDataDo);
					}
					questionRowIndex.add(collectionProgressCount);
				}else{
					resourceRowIndex.add(collectionProgressCount);
				}
				collectionProgressCount++;
			}
			setQuestionsData(questionsData);
			setQuestionsPrintData(questionsData);
			setOpenendedQuestionsData(openendedData);
			setOpenendedQuestionsPrintData(openendedData);
			setCollectionBreakDown(result,loadingImage);
			setCollectionBreakDownPrintData(result);
	}
	/**
	 * This will set the collections break down print data.
	 * @param result
	 */
	void setCollectionBreakDownPrintData(ArrayList<UserDataDo> result){
		try{
				printResourceData.clear();			
				UserDataDo maxAvgValue=Collections.max(result,new Comparator<UserDataDo>() {
		        	public int compare(UserDataDo o1, UserDataDo o2) {
		        		 Long obj1 = new Long(o1.getTimeSpent());
		        	     Long obj2 = new Long(o2.getTimeSpent());
		        	     return obj1.compareTo(obj2);
		        	}
		        });
		        UserDataDo maxViews=Collections.max(result,new Comparator<UserDataDo>() {
		        	public int compare(UserDataDo o1, UserDataDo o2) {
		        		 Integer obj1 = new Integer(o1.getViews());
		        		 Integer obj2 = new Integer(o2.getViews());
		        	     return obj1.compareTo(obj2);
		        	}
		        });
			    final DataTable data = DataTable.create();
			    data.addColumn(ColumnType.NUMBER, "No.");
		        data.addColumn(ColumnType.STRING, "Format");
		        data.addColumn(ColumnType.STRING, "Title");
		        data.addColumn(ColumnType.STRING, "Avg.Time&nbsp;Spent");
		        data.addColumn(ColumnType.STRING, "Views");
		        data.addColumn(ColumnType.STRING, "Reaction");
		        data.addRows(result.size());
		        
		        for(int i=0;i<result.size();i++) {
		        	data.setCell(i, 0,result.get(i).getItemSequence(), null, getPropertiesCell());
		            //set Format
		              String  resourceCategory =result.get(i).getCategory()!=null?result.get(i).getCategory():"";
		              String categoryStyle="";
					  if(resourceCategory.equalsIgnoreCase("website")){
					      resourceCategory = "webpage";
					      categoryStyle=res.css().category_new_type_webpage();
					  } else if(resourceCategory.equalsIgnoreCase("slide")){
					      resourceCategory = "image";
					      categoryStyle=res.css().category_new_type_image();
					  } else if(resourceCategory.equalsIgnoreCase("handout") || resourceCategory.equalsIgnoreCase("lesson") || resourceCategory.equalsIgnoreCase("textbook")) {
					      resourceCategory = "text";
					      categoryStyle=res.css().category_new_type_text();
					  }  else if(resourceCategory.equalsIgnoreCase("exam")) {
					      resourceCategory = "webpage";
					      categoryStyle=res.css().category_new_type_webpage();
					  } else if(resourceCategory.equalsIgnoreCase("video")) {
					      resourceCategory = "webpage";
					      categoryStyle=res.css().category_new_type_video();
					  } else if(resourceCategory.equalsIgnoreCase("interactive")) {
					      resourceCategory = "webpage";
					      categoryStyle=res.css().category_new_type_interactive();
					  } else{
						  categoryStyle=res.css().category_new_type_other();
					  }
		            Label categorylbl=new Label();
		            categorylbl.addStyleName(categoryStyle);
		            categorylbl.addStyleName(res.css().setMarginAuto());
		            data.setValue(i, 1,categorylbl.toString());
		            
		            //Set Question Title
		            Label questionTitle=new Label(AnalyticsUtil.html2text(result.get(i).getTitle()));
		            questionTitle.setStyleName(res.css().alignCenterAndBackground());
		            data.setValue(i, 2, questionTitle.toString());
		          
		           //Set time spent
		            HorizontalPanel timeSpentpnl=new HorizontalPanel();
		            timeSpentpnl.add(AnalyticsUtil.getTimeStampLabel(result.get(i).getTimeSpent()));
		            Label progressBar=new Label();
		            progressBar.setStyleName(res.css().setProgressBar());
		            timeSpentpnl.add(progressBar);
		            double maxAvgVal = ((double) result.get(i).getTimeSpent())/((double) maxAvgValue.getTimeSpent());
		            progressBar.getElement().getStyle().setWidth(maxAvgVal*100, Unit.PX);
		            data.setValue(i, 3, timeSpentpnl.toString());
		           
		            //set Views label
		            HorizontalPanel viewpnl=new HorizontalPanel();
		            Label viewlbl=new Label(Integer.toString(result.get(i).getViews()));
		            viewlbl.setStyleName(res.css().alignCenterAndBackground());
		            viewpnl.add(viewlbl);
		            Label viewProgressBar=new Label();
		            viewProgressBar.setStyleName(res.css().setProgressBar());
		            viewpnl.add(viewProgressBar);
		            float maxViewVal = ((float) result.get(i).getViews())/((float) maxViews.getViews());
		            viewProgressBar.getElement().getStyle().setWidth(maxViewVal*100, Unit.PX);
		            data.setValue(i, 4, viewpnl.toString());
		            
		            //Set reactions
		            int reaction=result.get(i).getReaction();
		            data.setValue(i, 5, new AnalyticsReactionWidget(reaction).toString());
		        }
		        final Options options = Options.create();
		        options.setAllowHtml(true);
		        Table table = new Table(data, options);
		        printResourceData.add(table);
		}catch(Exception e){
			
		}
	}
	/**
	 * This will set the collections break down data
	 * @param result
	 * @param loadingImage
	 */
	void setCollectionBreakDown(ArrayList<UserDataDo> result,HTMLPanel loadingImage){
		
		final int[] primitivesQuestions = AnalyticsUtil.toIntArray(questionRowIndex);
		final int[] primitivesResources = AnalyticsUtil.toIntArray(resourceRowIndex);
		
			UserDataDo maxAvgValue=Collections.max(result,new Comparator<UserDataDo>() {
	        	public int compare(UserDataDo o1, UserDataDo o2) {
	        		 Long obj1 = new Long(o1.getTimeSpent());
	        	     Long obj2 = new Long(o2.getTimeSpent());
	        	     return obj1.compareTo(obj2);
	        	}
	        });
	        UserDataDo maxViews=Collections.max(result,new Comparator<UserDataDo>() {
	        	public int compare(UserDataDo o1, UserDataDo o2) {
	        		 Integer obj1 = new Integer(o1.getViews());
	        		 Integer obj2 = new Integer(o2.getViews());
	        	     return obj1.compareTo(obj2);
	        	}
	        });
		    final DataTable data = DataTable.create();
		    data.addColumn(ColumnType.NUMBER, "No.");
	        data.addColumn(ColumnType.STRING, "Format");
	        data.addColumn(ColumnType.STRING, "Title");
	        data.addColumn(ColumnType.STRING, "Avg.Time&nbsp;Spent");
	        data.addColumn(ColumnType.STRING, "Views");
	        data.addColumn(ColumnType.STRING, "Reaction");
	        data.addRows(result.size());
	        
	        for(int i=0;i<result.size();i++) {
	        	data.setCell(i, 0, result.get(i).getItemSequence(), null, getPropertiesCell());
	            //set Format
	              String  resourceCategory =result.get(i).getCategory()!=null?result.get(i).getCategory():"";
	              String categoryStyle="";
				  if(resourceCategory.equalsIgnoreCase("website")){
				      resourceCategory = "webpage";
				      categoryStyle=res.css().category_new_type_webpage();
				  } else if(resourceCategory.equalsIgnoreCase("slide")){
				      resourceCategory = "image";
				      categoryStyle=res.css().category_new_type_image();
				  } else if(resourceCategory.equalsIgnoreCase("handout") || resourceCategory.equalsIgnoreCase("lesson") || resourceCategory.equalsIgnoreCase("textbook")) {
				      resourceCategory = "text";
				      categoryStyle=res.css().category_new_type_text();
				  }  else if(resourceCategory.equalsIgnoreCase("exam")) {
				      resourceCategory = "webpage";
				      categoryStyle=res.css().category_new_type_webpage();
				  } else if(resourceCategory.equalsIgnoreCase("video")) {
				      resourceCategory = "webpage";
				      categoryStyle=res.css().category_new_type_video();
				  } else if(resourceCategory.equalsIgnoreCase("interactive")) {
				      resourceCategory = "webpage";
				      categoryStyle=res.css().category_new_type_interactive();
				  } else{
					  categoryStyle=res.css().category_new_type_other();
				  }
	            Label categorylbl=new Label();
	            categorylbl.addStyleName(categoryStyle);
	            categorylbl.addStyleName(res.css().setMarginAuto());
	            data.setValue(i, 1,categorylbl.toString());
	            
	            //Set Question Title
	            Label questionTitle=new Label(AnalyticsUtil.html2text(result.get(i).getTitle()!=null?result.get(i).getTitle():""));
	            questionTitle.setStyleName(res.css().alignCenterAndBackground());
	            data.setValue(i, 2, questionTitle.toString());
	          
	           //Set time spent
	            HorizontalPanel timeSpentpnl=new HorizontalPanel();
	            timeSpentpnl.add(AnalyticsUtil.getTimeStampLabel(result.get(i).getTimeSpent()));
	            Label progressBar=new Label();
	            progressBar.setStyleName(res.css().setProgressBar());
	            timeSpentpnl.add(progressBar);
	            double maxAvgVal = ((double) result.get(i).getTimeSpent())/((double) maxAvgValue.getTimeSpent());
	            progressBar.getElement().getStyle().setWidth(maxAvgVal*100, Unit.PX);
	            data.setValue(i, 3, timeSpentpnl.toString());
	           
	            //set Views label
	            HorizontalPanel viewpnl=new HorizontalPanel();
	            Label viewlbl=new Label(Integer.toString(result.get(i).getViews()));
	            viewlbl.setStyleName(res.css().alignCenterAndBackground());
	            viewpnl.add(viewlbl);
	            Label viewProgressBar=new Label();
	            viewProgressBar.setStyleName(res.css().setProgressBar());
	            viewpnl.add(viewProgressBar);
	            float maxViewVal = ((float) result.get(i).getViews())/((float) maxViews.getViews());
	            viewProgressBar.getElement().getStyle().setWidth(maxViewVal*100, Unit.PX);
	            data.setValue(i, 4, viewpnl.toString());
	            
	            //Set reactions
	            int reaction=result.get(i).getReaction();
	            data.setValue(i, 5, new AnalyticsReactionWidget(reaction).toString());
	        }
	        final Options options = Options.create();
	        options.setAllowHtml(true);
	        Table table = new Table(data, options);
	        individualResourceBreakdownData.add(table);
	        table.getElement().getFirstChildElement().getFirstChildElement().getFirstChildElement().getStyle().setProperty("width", "98% !important");
	        filterDropDown.addChangeHandler(new ChangeHandler() {
	    		
				@Override
				public void onChange(ChangeEvent event) {
					  individualResourceBreakdownData.clear();
						int selectedIndex=filterDropDown.getSelectedIndex();
					 	operationsView=DataView.create(data);
						 if(selectedIndex==1){
							 operationsView.hideRows(primitivesResources); 
						 }
						 if(selectedIndex==2){
							 operationsView.hideRows(primitivesQuestions); 
						 }
						 Table table = new Table(operationsView, options);
					     table.setStyleName("collectionProgressTable");
					     individualResourceBreakdownData.add(table);	
					     table.addDomHandler(new ClickOnTableCell(), ClickEvent.getType());
				}
			});
	        if(loadingImage!=null)
	        loadingImage.setVisible(false);
	}
	/**
	 * This will set the openended questions data for printing purpose
	 * @param result
	 */
	void setOpenendedQuestionsPrintData(ArrayList<UserDataDo> result){
		try{
			printOpendedData.clear();		
		    DataTable data = DataTable.create();
		    data.addColumn(ColumnType.NUMBER, "No.");
	        data.addColumn(ColumnType.STRING, "Question");
	        data.addColumn(ColumnType.STRING, "Completion");
	        data.addColumn(ColumnType.STRING, "Time&nbsp;Spent");
	        data.addColumn(ColumnType.STRING, "Reaction");
	        data.addColumn(ColumnType.STRING, "Response");
	        data.addRows(result.size());
	        for(int i=0;i<result.size();i++) {
	        	data.setCell(i, 0, result.get(i).getItemSequence(), null, getPropertiesCell());
	        	
	            //Set Question Title
	            Label questionTitle=new Label( AnalyticsUtil.html2text(result.get(i).getTitle()));
	            questionTitle.setStyleName(res.css().alignCenterAndBackground());
	            data.setValue(i, 1, questionTitle.toString());
	          
	            //Set completion
	            int noOfAttempts=result.get(i).getAttempts();
	            Label completion=new Label();
	            completion.setStyleName(res.css().alignCenterAndBackground());
	            if(noOfAttempts>0){
	            	completion.setText("Completed");
	            }else{
	            	completion.setText("In Progress");
	            }
	            data.setValue(i, 2, completion.toString());
	          
	            //Set time spent
	            data.setValue(i, 3, AnalyticsUtil.getTimeStampLabel(result.get(i).getTimeSpent()).toString());
	           
	            //Set reactions
	            int reaction=result.get(i).getReaction();
	            data.setValue(i, 4, new AnalyticsReactionWidget(reaction).toString());
	           
	            //set View response label
	            Label viewResponselbl=new Label(result.get(i).getText());
	            viewResponselbl.setStyleName(res.css().viewResponseTextOpended());
	            data.setValue(i, 5, viewResponselbl.toString());
	        }
	        Options options = Options.create();
	        options.setAllowHtml(true);
	        final Table table = new Table(data, options);
	        printOpendedData.add(table);
	        if(result.size()==0){
	        	Label erroeMsg=new Label();
	        	erroeMsg.setStyleName(res.css().displayMessageTextForOEQuestions());
	        	erroeMsg.setText("It looks like there is no open-ended question data for this collection yet.");
	        	printOpendedData.add(erroeMsg);
	        }
		}catch(Exception e){
			
		}
	}
	/**
	 * This will set the openended questions data
	 * @param result
	 */
	void setOpenendedQuestionsData(ArrayList<UserDataDo> result){
		    DataTable data = DataTable.create();
		    data.addColumn(ColumnType.NUMBER, "No.");
	        data.addColumn(ColumnType.STRING, "Question");
	        data.addColumn(ColumnType.STRING, "Completion");
	        data.addColumn(ColumnType.STRING, "Time&nbsp;Spent");
	        data.addColumn(ColumnType.STRING, "Reaction");
	        data.addColumn(ColumnType.STRING, "Response");
	        data.addRows(result.size());
	        for(int i=0;i<result.size();i++) {
	        	data.setCell(i, 0, result.get(i).getItemSequence(), null, getPropertiesCell());
	        	
	            //Set Question Title
	            Label questionTitle=new Label( AnalyticsUtil.html2text(result.get(i).getTitle()));
	            questionTitle.setStyleName(res.css().alignCenterAndBackground());
	            data.setValue(i, 1, questionTitle.toString());
	          
	            //Set completion
	            int noOfAttempts=result.get(i).getAttempts();
	            Label completion=new Label();
	            completion.setStyleName(res.css().alignCenterAndBackground());
	            if(noOfAttempts>0){
	            	completion.setText("Yes");
	            }else{
	            	completion.setText("No");
	            }
	            data.setValue(i, 2, completion.toString());
	          
	            //Set time spent
	            data.setValue(i, 3, AnalyticsUtil.getTimeStampLabel(result.get(i).getTimeSpent()).toString());
	           
	            //Set reactions
	            int reaction=result.get(i).getReaction();
	            data.setValue(i, 4, new AnalyticsReactionWidget(reaction).toString());
	           
	            //set View response label
	            Label viewResponselbl=new Label(VIEWRESPONSE);
	            viewResponselbl.setStyleName(res.css().viewResponseTextOpended());
	            viewResponselbl.getElement().setAttribute("resourceGooruId", result.get(i).getResourceGooruOId());
   	            viewResponselbl.getElement().setAttribute("questionType", result.get(i).getType());
	            data.setValue(i, 5, viewResponselbl.toString());
	        }
	        Options options = Options.create();
	        options.setAllowHtml(true);
	        final Table table = new Table(data, options);
	        individualOpenendedData.add(table);
	        if(result.size()==0){
	        	Label erroeMsg=new Label();
	        	erroeMsg.setStyleName(res.css().displayMessageTextForOEQuestions());
	        	erroeMsg.setText("It looks like there is no open-ended question data for this collection yet.");
	        	individualOpenendedData.add(erroeMsg);
	        }
	        table.addDomHandler(new ClickOnTableCell(), ClickEvent.getType());
	        table.getElement().getFirstChildElement().getFirstChildElement().getFirstChildElement().getStyle().setProperty("width", "98% !important");
	}
	/**
	 * This class is used to handle the click event on the table cell
	 */
	class ClickOnTableCell implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			Element ele=event.getNativeEvent().getEventTarget().cast();
			if(ele.getInnerText().equalsIgnoreCase(VIEWRESPONSE)){
				getUiHandlers().setOEtextData(ele.getAttribute("resourceGooruId"),ele.getAttribute("questionType"));
			}
		}
	}
	/**
	 * This will set the question data for printing purpose
	 * @param result
	 */
	void setQuestionsPrintData(ArrayList<UserDataDo> result){
		try{
			printScoredData.clear();
			boolean isTickdisplay=false;
		    DataTable data = DataTable.create();
		    data.addColumn(ColumnType.NUMBER, "No.");
	        data.addColumn(ColumnType.STRING, "Question");
	        data.addColumn(ColumnType.STRING, "Score");
	        data.addColumn(ColumnType.STRING, "Student&nbsp;Answer");
	        data.addColumn(ColumnType.STRING, "#Attempts");
	        data.addColumn(ColumnType.STRING, "Time&nbsp;Spent");
	        data.addColumn(ColumnType.STRING, "Reactions");
	        
	        data.addRows(result.size());
	        if(result.size()!=0){
				        for(int i=0;i<result.size();i++) {
				        	isTickdisplay=false;
				            data.setCell(i, 0, result.get(i).getItemSequence(), null, getPropertiesCell());
				           
				            Label questionTitle=new Label(AnalyticsUtil.html2text(result.get(i).getTitle()));
				            questionTitle.setStyleName(res.css().alignCenterAndBackground());
				            data.setValue(i, 1, questionTitle.toString());
				            int noOfAttempts=result.get(i).getAttempts();
				           
				            
				            //Set Answer choices
				            String questionType= result.get(i).getType();
				            String correctAnser = null;
				        	if(questionType.equalsIgnoreCase("MC") ||questionType.equalsIgnoreCase("TF")){ 
				        		Label anserlbl=new Label();
				        		if(result.get(i).getMetaData()!=null && result.get(i).getOptions()!=null){
				        			 JSONValue value = JSONParser.parseStrict(result.get(i).getOptions());
				        			 JSONObject authorObject = value.isObject();
				        			 if(authorObject.keySet().size()!=0){
				        				 String userSelectedOption=authorObject.keySet().iterator().next();
					        			 correctAnser=getCorrectAnswer(result.get(i).getMetaData());
					        			 if(userSelectedOption!=null && correctAnser!=null){
					        				 anserlbl.setText(userSelectedOption);
					        				 if(userSelectedOption.equalsIgnoreCase(correctAnser) && noOfAttempts==1){
					        					 anserlbl.getElement().getStyle().setColor(CORRECT);
					        					 isTickdisplay=true;
					        				 }else if(userSelectedOption.equalsIgnoreCase(correctAnser) && noOfAttempts>1){
					        					 anserlbl.getElement().getStyle().setColor(ONMULTIPULEATTEMPTS);
					        				 }else{
					        					 anserlbl.getElement().getStyle().setColor(INCORRECT);
					        				 }
					        			 }
				        			 }
				        		}
				        		anserlbl.setStyleName(res.css().alignCenterAndBackground());
				        		data.setValue(i, 3, anserlbl.toString());
				        	}else if (questionType.equalsIgnoreCase("FIB")){
				        		VerticalPanel answerspnl=new VerticalPanel();
				        		if(result.get(i).getMetaData()!=null && result.get(i).getOptions()!=null){
				        			String answerTextFormat = "";
				        			String[] answersArry = null;
				        			ArrayList<MetaDataDo> questionList=result.get(i).getMetaData();
									for (MetaDataDo metaDataDo : questionList) {
										String answerText = "";
										if((metaDataDo.getAnswer_text() != null)) {
											answerText = metaDataDo.getAnswer_text();
										}
										answerTextFormat += '[' + answerText +']';
										if(questionList.size()  != metaDataDo.getSequence()){
											  answerTextFormat += ",";
										}
									}
									String[] userFibOption = null;
									if(result.get(i).getText() != null) {
										answersArry=answerTextFormat.split(",");
										userFibOption =result.get(i).getText().split(",");
									}
									if(answersArry!=null && userFibOption!=null){
										boolean isCorrect=false;
										for (int k = 0; k < answersArry.length; k++) { 
											Label answerChoice=new Label();
											if(answersArry[k]!=null && k<userFibOption.length){
												if((answersArry[k].toLowerCase().trim().equalsIgnoreCase(userFibOption[k].toLowerCase().trim())) && (noOfAttempts == 1)){
													answerChoice.setText(userFibOption[k]);
													answerChoice.getElement().getStyle().setColor(CORRECT);
												}else if((answersArry[k].toLowerCase().trim().equalsIgnoreCase(userFibOption[k].toLowerCase().trim())) && (noOfAttempts > 1)) {
													answerChoice.setText(userFibOption[k]);
													answerChoice.getElement().getStyle().setColor(ONMULTIPULEATTEMPTS);
													isCorrect=true;
												}else{
													answerChoice.setText(userFibOption[k]);
													answerChoice.getElement().getStyle().setColor(INCORRECT);
													isCorrect=true;
												}
												answerChoice.setStyleName(res.css().alignCenterAndBackground());
												answerspnl.add(answerChoice);
											}
										}
										if(!isCorrect){
											 isTickdisplay=true;
										}
									}
				        		}
				        		 answerspnl.setStyleName(res.css().setMarginAuto());
				        		 data.setValue(i, 3, answerspnl.toString());
				        	}else  if(questionType.equalsIgnoreCase("MA")){
				        		VerticalPanel answerspnl=new VerticalPanel();
				        		if(result.get(i).getAnswerObject()!=null) {
				        			 JSONValue value = JSONParser.parseStrict(result.get(i).getAnswerObject());
				        			 JSONObject answerObject = value.isObject();
				        			 Set<String> keys=answerObject.keySet();
				        			 Iterator<String> itr = keys.iterator();
				        			 boolean isCorrect=false;
				        		      while(itr.hasNext()) {
				        		         JSONArray attemptsObj=(JSONArray) answerObject.get(itr.next().toString());
				        		         for(int j=0;j<attemptsObj.size();j++){
				        		        	Label answerChoice=new Label();
				        		            String showMessage = null;
				        		            boolean skip = attemptsObj.get(j).isObject().get("skip").isBoolean().booleanValue();
				        		        	String status =attemptsObj.get(j).isObject().get("status").isString().stringValue();
				        		        	String matext =attemptsObj.get(j).isObject().get("text").isString().stringValue();
				 	        		         if(skip == false)
				 							  {
				 	        		        	if(matext.equalsIgnoreCase("0")) {
													  showMessage = "No";
												} else if(matext.equalsIgnoreCase("1")) {
													   showMessage = "Yes";
												}
												answerChoice.setText(showMessage);
				 									if(status.equalsIgnoreCase("0")) {
				 										answerChoice.getElement().getStyle().setColor(INCORRECT);
				 										 isCorrect=true;
				 									} else if(status.equalsIgnoreCase("1") && (noOfAttempts == 1)) {
				 										answerChoice.getElement().getStyle().setColor(CORRECT);
				 									} else if(status.equalsIgnoreCase("1") && (noOfAttempts > 1)) {
				 										answerChoice.getElement().getStyle().setColor(ONMULTIPULEATTEMPTS);
				 									}
				 							  }else{
				 								 isCorrect=true;
				 							  }
				 	        		        answerChoice.setStyleName(res.css().alignCenterAndBackground());
				 	        		        answerspnl.add(answerChoice);
				        		         }
				        		      }
				        		      if(!isCorrect){
											 isTickdisplay=true;
										}
				        		}
				        		 answerspnl.setStyleName(res.css().setMarginAuto());
				        		 data.setValue(i, 3, answerspnl.toString());
				        	}
				        	
				        	Image correctImg=new Image();
				        	if(isTickdisplay){
				        		 correctImg.setUrl("/images/analytics/tick.png");
				        	}else{ 
				        		correctImg.setUrl("/images/analytics/wrong.png");				        		
				        	}
				            data.setCell(i, 2, correctImg.toString(), null, getPropertiesCell());
				            //Set attempts
				            Label attempts=new Label(Integer.toString(noOfAttempts));
				            attempts.setStyleName(res.css().alignCenterAndBackground());
				            data.setValue(i, 4, attempts.toString());
				            
				            //Set time spent
				            data.setValue(i, 5,AnalyticsUtil.getTimeStampLabel(result.get(i).getTimeSpent()).toString());
				            
				            //Set reactions
				            int reaction=result.get(i).getReaction();
				            data.setValue(i, 6, new AnalyticsReactionWidget(reaction).toString());
				        }
				}
	        Options options = Options.create();
	        options.setAllowHtml(true);
	        Table table = new Table(data, options);
	        printScoredData.add(table);
	        if(result.size()==0){
	        	Label erroeMsg=new Label();
	        	erroeMsg.setStyleName(res.css().displayMessageTextForScoredQuestions());
	        	erroeMsg.setText("It looks like there is no scored question data for this collection yet.");
	        	printScoredData.add(erroeMsg);
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * This will set the question data
	 * @param result
	 */
	void setQuestionsData(ArrayList<UserDataDo> result){
		    DataTable data = DataTable.create();
		    data.addColumn(ColumnType.NUMBER, "No.");
	        data.addColumn(ColumnType.STRING, "Question");
	        data.addColumn(ColumnType.STRING, "Answer&nbsp;Choice");
	        data.addColumn(ColumnType.STRING, "#Attempts");
	        data.addColumn(ColumnType.STRING, "Time&nbsp;Spent");
	        data.addColumn(ColumnType.STRING, "Reactions");
	      
	        data.addRows(result.size());
	      if(result.size()!=0){
				        for(int i=0;i<result.size();i++) {
				            data.setCell(i, 0, result.get(i).getItemSequence(), null, getPropertiesCell());
				           
				            Label questionTitle=new Label(AnalyticsUtil.html2text(result.get(i).getTitle()));
				            questionTitle.setStyleName(res.css().alignCenterAndBackground());
				            data.setValue(i, 1, questionTitle.toString());
				            int noOfAttempts=result.get(i).getAttempts();
				           
				            //Set Answer choices
				            String questionType= result.get(i).getType();
				            String correctAnser = null;
				        	if(questionType.equalsIgnoreCase("MC") ||questionType.equalsIgnoreCase("TF")){ 
				        		Label anserlbl=new Label();
				        		if(result.get(i).getMetaData()!=null && result.get(i).getOptions()!=null){
				        			 JSONValue value = JSONParser.parseStrict(result.get(i).getOptions());
				        			 JSONObject authorObject = value.isObject();
				        			 if(authorObject.keySet().size()!=0){
				        				 String userSelectedOption=authorObject.keySet().iterator().next();
					        			 correctAnser=getCorrectAnswer(result.get(i).getMetaData());
					        			 if(userSelectedOption!=null && correctAnser!=null){
					        				 anserlbl.setText(userSelectedOption);
					        				 if(userSelectedOption.equalsIgnoreCase(correctAnser) && noOfAttempts==1){
					        					 anserlbl.getElement().getStyle().setColor(CORRECT);
					        				 }else if(userSelectedOption.equalsIgnoreCase(correctAnser) && noOfAttempts>1){
					        					 anserlbl.getElement().getStyle().setColor(ONMULTIPULEATTEMPTS);
					        				 }else{
					        					 anserlbl.getElement().getStyle().setColor(INCORRECT);
					        				 }
					        			 }
				        			 }
				        		}
				        		anserlbl.setStyleName(res.css().alignCenterAndBackground());
				        		data.setValue(i, 2, anserlbl.toString());
				        	}else if (questionType.equalsIgnoreCase("FIB")){
				        		VerticalPanel answerspnl=new VerticalPanel();
				        		if(result.get(i).getMetaData()!=null && result.get(i).getOptions()!=null){
				        			String answerTextFormat = "";
				        			String[] answersArry = null;
				        			ArrayList<MetaDataDo> questionList=result.get(i).getMetaData();
									for (MetaDataDo metaDataDo : questionList) {
										String answerText = "";
										if((metaDataDo.getAnswer_text() != null)) {
											answerText = metaDataDo.getAnswer_text();
										}
										answerTextFormat += '[' + answerText +']';
										if(questionList.size()  != metaDataDo.getSequence()){
											  answerTextFormat += ",";
										}
									}
									String[] userFibOption = null;
									if(result.get(i).getText() != null) {
										answersArry=answerTextFormat.split(",");
										userFibOption =result.get(i).getText().split(",");
									}
									if(answersArry!=null && userFibOption!=null){
										for (int k = 0; k < answersArry.length; k++) { 
											Label answerChoice=new Label();
											if(answersArry[k]!=null && k<userFibOption.length){
												if((answersArry[k].toLowerCase().trim().equalsIgnoreCase(userFibOption[k].toLowerCase().trim())) && (noOfAttempts == 1)){
													answerChoice.setText(userFibOption[k]);
													answerChoice.getElement().getStyle().setColor(CORRECT);
												}else if((answersArry[k].toLowerCase().trim().equalsIgnoreCase(userFibOption[k].toLowerCase().trim())) && (noOfAttempts > 1)) {
													answerChoice.setText(userFibOption[k]);
													answerChoice.getElement().getStyle().setColor(ONMULTIPULEATTEMPTS);
												}else{
													answerChoice.setText(userFibOption[k]);
													answerChoice.getElement().getStyle().setColor(INCORRECT);
												}
												answerChoice.setStyleName(res.css().alignCenterAndBackground());
												answerspnl.add(answerChoice);
											}
										}
									}
				        		}
				        		 answerspnl.setStyleName(res.css().setMarginAuto());
				        		 data.setValue(i, 2, answerspnl.toString());
				        	}else  if(questionType.equalsIgnoreCase("MA")){
				        		VerticalPanel answerspnl=new VerticalPanel();
				        		if(result.get(i).getAnswerObject()!=null) {
				        			 JSONValue value = JSONParser.parseStrict(result.get(i).getAnswerObject());
				        			 JSONObject answerObject = value.isObject();
				        			 Set<String> keys=answerObject.keySet();
				        			 Iterator<String> itr = keys.iterator();
				        		      while(itr.hasNext()) {
				        		         JSONArray attemptsObj=(JSONArray) answerObject.get(itr.next().toString());
				        		         for(int j=0;j<attemptsObj.size();j++){
				        		        	Label answerChoice=new Label();
				        		            String showMessage = null;
				        		            boolean skip = attemptsObj.get(j).isObject().get("skip").isBoolean().booleanValue();
				        		        	String status =attemptsObj.get(j).isObject().get("status").isString().stringValue();
				        		        	String matext =attemptsObj.get(j).isObject().get("text").isString().stringValue();
				 	        		         if(skip == false)
				 							  {
				 	        		        	if(matext.equalsIgnoreCase("0")) {
													  showMessage = "No";
												} else if(matext.equalsIgnoreCase("1")) {
													   showMessage = "Yes";
												}
												answerChoice.setText(showMessage);
				 									if(status.equalsIgnoreCase("0")) {
				 										answerChoice.getElement().getStyle().setColor(INCORRECT);
				 									} else if(status.equalsIgnoreCase("1") && (noOfAttempts == 1)) {
				 										answerChoice.getElement().getStyle().setColor(CORRECT);
				 									} else if(status.equalsIgnoreCase("1") && (noOfAttempts > 1)) {
				 										answerChoice.getElement().getStyle().setColor(ONMULTIPULEATTEMPTS);
				 									}
				 							  }
				 	        		        answerChoice.setStyleName(res.css().alignCenterAndBackground());
				 	        		        answerspnl.add(answerChoice);
				        		         }
				        		      }
				        		}
				        		 answerspnl.setStyleName(res.css().setMarginAuto());
				        		 data.setValue(i, 2, answerspnl.toString());
				        	}
				           
				            //Set attempts
				            Label attempts=new Label(Integer.toString(noOfAttempts));
				            attempts.setStyleName(res.css().alignCenterAndBackground());
				            data.setValue(i, 3, attempts.toString());
				            
				            //Set time spent
				            data.setValue(i, 4, AnalyticsUtil.getTimeStampLabel(result.get(i).getTimeSpent()).toString());
				            
				            //Set reactions
				            int reaction=result.get(i).getReaction();
				            data.setValue(i, 5, new AnalyticsReactionWidget(reaction).toString());
				        }
				}
	        Options options = Options.create();
	        options.setAllowHtml(true);
	        Table table = new Table(data, options);
	        table.getElement().setId("individulaDataScored");
	        individualScoredData.add(table);
	        if(result.size()==0){
	        	Label erroeMsg=new Label();
	        	erroeMsg.setStyleName(res.css().displayMessageTextForScoredQuestions());
	        	erroeMsg.setText("It looks like there is no scored question data for this collection yet.");
	        	individualScoredData.add(erroeMsg);
	        }
	        table.getElement().getFirstChildElement().getFirstChildElement().getFirstChildElement().getStyle().setProperty("width", "98% !important");
	}
	/**
	 * This will return the correct answers
	 * @param metaDataObj
	 * @return
	 */
	String getCorrectAnswer(ArrayList<MetaDataDo> metaDataObj){
		for (MetaDataDo metaDataDo : metaDataObj) {
			if(metaDataDo.getIs_correct()==1){
				return AnalyticsUtil.getCharForNumber(metaDataDo.getSequence()-1);
			}
		}
		return null;
	}
	/**
	 * This will set the cell properties
	 * @return
	 */
	com.google.gwt.visualization.client.Properties getPropertiesCell(){
		  Properties properties=Properties.create();
		  properties.set("style", "text-align:center;");
		  com.google.gwt.visualization.client.Properties p=properties.cast();
		  return p;
	}
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummaryIndividual.IsCollectionSummaryIndividualView#setIndividualCollectionMetaData(java.util.ArrayList, org.ednovo.gooru.shared.model.analytics.PrintUserDataDO)
	 */
	@Override
	public void setIndividualCollectionMetaData(
			ArrayList<CollectionSummaryMetaDataDo> result,PrintUserDataDO printUserDataDO) {
		//Set collection meta data
		collectionTitle=result.get(0).getTitle();
		totalTimeSpentlbl.setText(AnalyticsUtil.getTimeSpent(result.get(0).getAvgTimeSpent()));
		totalViewlbl.setText(Integer.toString(result.get(0).getViews()));
		totalAvgReactionlbl.clear();
		totalAvgReactionlbl.add(new AnalyticsReactionWidget(result.get(0).getAvgReaction()));
		collectionOverViewWidget.setData(result.get(0),false);
		collectionSummaryWidget.setDataAnalyticsData(result.get(0),printUserDataDO);
	}
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummaryIndividual.IsCollectionSummaryIndividualView#setViewResponseData(java.util.ArrayList, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void setViewResponseData(ArrayList<OetextDataDO> result,	String resourceGooruId, String collectionId, String classpageId,String pathwayId, String questionType,boolean isSummary,String session) {
		 popupPanel=new ViewResponsesPopup(result,resourceGooruId,collectionId,classpageId,pathwayId,questionType,isSummary,session);
		 popupPanel.setStyleName(res.css().setOETextPopupCenter());
	     if(popupPanel.isShowing()){
	    	 popupPanel.hide();
	    	 Window.enableScrolling(true);
	     }else{
	    	 Window.enableScrolling(false);
	    	 popupPanel.setGlassEnabled(true);
	    	 popupPanel.setGlassStyleName(res.css().setGlassStyleName());
	    	 popupPanel.setAutoHideEnabled(true);
	    	 popupPanel.show();
	    	 popupPanel.center();
	     }
	}
	/**
	 * This method is used for printing purpose
	 * @param isClickedOnSave
	 * @param isClickedOnEmail
	 */
	public void setPrintIndividualSummayData(boolean isClickedOnSave,boolean isClickedOnEmail){
		printWidget.clear();
		Label collectionSummaryText=new Label();
		collectionSummaryText.setText(i18n.GL1587());
		collectionSummaryText.getElement().getStyle().setPaddingBottom(15, Unit.PX);
		collectionSummaryText.addStyleName("collectionSummaryText");
		printWidget.add(collectionSummaryText);
		printWidget.add(collectionSummaryWidget);
		
		//To add scored questions
		Label scoredQuestionHeading=new Label();
		scoredQuestionHeading.setText("Scored Questions");
		scoredQuestionHeading.getElement().getStyle().setClear(Clear.BOTH);
		scoredQuestionHeading.getElement().getStyle().setPaddingTop(15, Unit.PX);
		scoredQuestionHeading.getElement().getStyle().setPaddingBottom(20, Unit.PX);
		printWidget.add(scoredQuestionHeading);
		printWidget.add(printScoredData);
		printScoredData.getElement().getStyle().setPaddingBottom(20, Unit.PX);
		//To add OE questions
		Label opendedQuestionHeading=new Label();
		opendedQuestionHeading.setText("OE Questions");
		opendedQuestionHeading.getElement().getStyle().setPaddingBottom(20, Unit.PX);
		printWidget.add(opendedQuestionHeading);
		printWidget.add(printOpendedData);
		printOpendedData.getElement().getStyle().setPaddingBottom(20, Unit.PX);
		//To add resource breakdown
		printWidget.add(collectionOverViewWidget);
		printWidget.add(printResourceData);
		if(isClickedOnSave){
				 getUiHandlers().setHtmltopdf(style.toString().replaceAll("'", "\\\\\"")+printWidget.getElement().getInnerHTML().toString().replaceAll("\"", "\\\\\""),collectionTitle,isClickedOnEmail);
				 printWidget.clear();
		}else{
			Print.it(style,printWidget);
		    printWidget.clear();
		}
	}
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummaryIndividual.IsCollectionSummaryIndividualView#setPdfForEmail(java.lang.String)
	 */
	@Override 
	public void setPdfForEmail(String path){
		if(emailPopup!=null){
			emailPopup.setEmailData(collectionTitle,path);
		}
	}
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummaryIndividual.IsCollectionSummaryIndividualView#enableAndDisableEmailButton(boolean)
	 */
	@Override
	public void enableAndDisableEmailButton(boolean isSummary){
		individualTabContainer.getEmailButton().setVisible(!isSummary);
	}
	@Override
	public void setErrorMessage() {
		noErrorMesage.setVisible(true);
		maincontainer.setVisible(false);
	}
	@Override
	public Frame getFrame(){
		return downloadFile;
	}
}
