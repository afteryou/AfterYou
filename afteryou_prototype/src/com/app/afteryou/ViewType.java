
package com.app.afteryou;

public final class ViewType {
    public static final int NONE = 0;

/*    // Find Root view
    SC_ViewType_FindRootView = 10,                      !< The find type selection view. 

    // Carousel
    SC_ViewType_CarouselBegin = 100,
    SC_ViewType_CarouselView,                           !< The carousel view. 
    SC_ViewType_CarouselEnd,

    // Navigation
    SC_ViewType_NavigationBegin = 200,
    SC_ViewType_Navigation3DView,                       !< The navigation 3d view. 
    SC_ViewType_Navigation2DView,                       !< The navigation 2d view. 
    SC_ViewType_NavigationCurrentDetailsView,           !< The current details view 
    SC_ViewType_NavigationDashboardView,                !< The navigation dashboard view. 
    SC_ViewType_NavigationDetourAroundView,             !< The detour around view 
    SC_ViewType_NavigationDetourAroundDistanceView,     !< The detour around distance view 
    SC_ViewType_NavigationDetourAroundRoadView,         !< The detour around road view 
    SC_ViewType_NavigationDetourAroundTrafficView,      !< The detour around traffic view 
    SC_ViewType_NavigationDetourDetailsView,            !< The detour details view 
    SC_ViewType_NavigationDetourIncidentDetailView,     !< The detour incident detail view 
    SC_ViewType_NavigationDetourIncidentListView,       !< The detour incident list view 
    SC_ViewType_NavigationDirectionsView,               !< The navigation directions view 
    SC_ViewType_NavigationIncidentDetailView,           !< The incident detail view 
    SC_ViewType_NavigationIncidentListView,             !< The incident list view 
    SC_ViewType_NavigationListView,                     !< The navigation list view 
    SC_ViewType_NavigationTurnSummaryView,
    SC_ViewType_NavigationLocationInfoView,             !< The location info view 
    SC_ViewType_NavigationLookAheadView,                !< The navigation look ahead view 
    SC_ViewType_NavigationLookAheadOutOffRoute,         !< The navigation look ahead out off route view 
    SC_ViewType_NavigationPlanTripView,                 !< The navigation plan trip view 
    SC_ViewType_NavigationTripPreviewView,              !< The navigation trip preview view 
    SC_ViewType_NavigationTripSummaryView,              !< The navigation trip summary view 
    SC_ViewType_NavigationProgressView,                 !< The navigation progress view 
        
    // I think the navigation controller could manager the views between SC_ViewType_NavigationBegin and End.
    SC_ViewType_MapTripSummary,                         !< Trip summary map view 
    SC_ViewType_MapDetour,                              !< Detour map view 
    SC_ViewType_MapPedestrian,                          !< Pedestrian navigation map view 
    SC_ViewType_MapTripSimple,
    SC_ViewType_MapTripGpsAccuracy,
    SC_ViewType_NavigationEnd,

    // Find
    SC_ViewType_FindBegin = 300,
    SC_ViewType_PoiView,                                !< The places selection view. 
    SC_ViewType_PoiResultListView,                      !< The search result view. 
    SC_ViewType_PoiResultListFuelView,                  !< The search result view for Fuel Stations. 
    SC_ViewType_PoiDetailView,                          !< The detail view of POI. 
    SC_ViewType_PoiDetailFuelView,                      !< The detail view of Fuel. 
    SC_ViewType_MovieView = 310,                        !< The movie selection view. 
    SC_ViewType_MovieResultListView,                    !< The search result view for movie
    SC_ViewType_MovieDetailView,                        !< The detail view of Movie. 
    SC_ViewType_MovieShowtimeResultListView,            !< The showtime results for movie. 
    SC_ViewType_MovieShowtimeDetailView,                !< The showtime detail for movie. 
    SC_ViewType_EventView = 320,                        !< The Event View. 
    SC_ViewType_EventListView,                          !< The Event list view.
    SC_ViewType_EventResultListView,                    !< The event result list view. 
    SC_ViewType_EventKeywordResultListView,             !< The event result list view with Keyword. 
    SC_ViewType_EventDetailView,                        !< the event detail view. 
    SC_ViewType_WeatherListView = 330,                  !< The weather list view. 
    SC_ViewType_WeatherDetailView,                      !< The weather detail view. 
    SC_ViewType_FindEnd,

    // LocWizard
    SC_ViewType_LocWizardBegin = 400,
    SC_ViewType_FindAddressRootView,                    !< The search address view. 
    SC_ViewType_FindAddress_AddFavorite_RootView,       !< The search address view from add favorite. 
    SC_ViewType_FindAddress_FindPoi_RootView,           !< The search address view. 
    SC_ViewType_FindAddress_SelectRouteDest_RootView,   !< The search address view. 
    SC_ViewType_FindAddress_DuringNavigation_RootView,  !< The search address view. 
    SC_ViewType_FindAddressRecentsView = 410,           !< The search recent view. 
    SC_ViewType_FindAddressRecentsView_LocWizard,       !< The search recent view in LocWizard status. 
    SC_ViewType_FindAddressRecents_Detail_View,         !< The search recent detail view. 
    SC_ViewType_FindAddressMySearchView,                !< The search mysearch view
    SC_ViewType_FindAddressFavoritesView = 420,         !< The search favorite view. 
    SC_ViewType_FindAddressFavoritesView_LocWizard,     !< The search favorite view in LocWizard status.. 
    SC_ViewType_FindAddressFavoritesView_Dest_Locwizard,!< The search favorite view from select dest
    SC_ViewType_FindAddressFavorites_Detail_View,       !< The search favorite detail view. 
    SC_ViewType_FindAddressFavorites_AddView,           !< The add favorite view. 
    SC_ViewType_FindAddressFavorites_AddHomeView,       !< The add home favorite view. 
    SC_ViewType_FindAddressFavorites_AddWorkView,       !< The add work favorite view. 
    SC_ViewType_FindAddressFavorites_EditView,          !< The edit favorite view. 
    SC_ViewType_FindAddressFavorites_EditHomeView,      !< The edit home favorite view. 
    SC_ViewType_FindAddressFavorites_EditWorkView,      !< The edit work favorite view. 
    SC_ViewType_FindAddressContactsView = 450,          !< The search contacts view. 
    SC_ViewType_FindAirportView = 460,                  !< The search airport view. 
    SC_ViewType_FindAirportView_Locwizard,              !< The search airport view in locwizard status. 
    SC_ViewType_FindAirportDetailView,                  !< The airport detail view. 
    SC_ViewType_CountryListView = 470,                  !< The country select view. 
    SC_ViewType_CountryListView_Airport,                !< The country view that from airport view
    SC_ViewType_MapDetailView,                          !< The country view that from airport view
    SC_ViewType_FindAddress_ShareView,                  !< The share view that invokes loc wizard
    SC_ViewType_LocWizardEnd,


    // Geocode
    SC_ViewType_GeocodeBegin = 500,
    SC_ViewType_GeocodeListView,                        ! The geocode list view. 
    SC_ViewType_GeocodeDetailView,                      ! The geocode detail view. 
    SC_ViewType_GeocodeDetailView_FromList,             ! The geocode detail view that enter through geocode list view. 
    SC_ViewType_GeocodeEnd,
    
    // Share
    SC_ViewType_ShareBegin = 600,
    SC_ViewType_ShareRootView,                          !< The share operations selection view. 
    SC_ViewType_SharePlaceView,                         !< The check new message view. 
    SC_ViewType_UpdateFacebookStatusView,               !< The update facebook status view. 
    SC_ViewType_FacebookEditLocationView,               !< The facebook edit location view. 
    SC_ViewType_InboxView,                              !< The inbox view. 
    SC_ViewType_SentView,                               !< The sent view. 
    SC_ViewType_InboxDetailView,                        !< The inbox detail view. 
    SC_ViewType_SentDetailView,                         !< The sent detail view. 
    SC_ViewType_ShareEnd,
*/    
    // Map
    public static final int MAP_BEGIN = 700;
    public static final int MAP_MAIN_VIEW = 701;                            /*!< Main map view */
    public static final int MAP_ROOT_VIEW = 702;                            /*!< Root Map view */
    public static final int MAP_INCIDENT_LIST_VIEW = 703;                   /*!< Incident list view of map */
    public static final int MAP_INCIDENT_DETAIL_VIEW = 704;                  /*!< Incident detail view of map */
    
    /* trip summary, detour and navigation view types are handled by the navigation */
    
    public static final int MAP_END = 705;
    
    // Startup
    public static final int STARTUP_BEGIN = 800;
    public static final int DISCLAIMER_VIEW = 801;                         //!< Disclaimer view 
    public static final int SPLASH_VIEW = 802;                             //!< Launch Splash view 
    public static final int MDN_VIEW = 803;                                //!< MDN input view 
    public static final int CONFIRM_EXIT_VIEW = 804;                       // !< Confirm exit View 
    public static final int STRATUP_END = 805;

    /*    
    SC_ViewType_DummyView = 900,                        !< Dummy (empty) View 
    
    // Message View
    SC_ViewType_MessageView = 910,                      !< Message view 
*/    
    //Preferences need more to do
    public static final int PREFERENCES_BEGIN = 1000;
    public static final int PREFERENCES_3D_CITIES_SET_UP = 1001;
    public static final int PREFERENCES_3D_CITIES = 1002; 
    public static final int PREFERENCES_ROUTE_OPTIONS = 1003;
    public static final int PREFERENCES = 1004;
    public static final int PREFERENCES_CITY_LIST = 1005;
    public static final int PREFERENCES_REGIONAL = 1006;
    public static final int DEVTOOLS_GPS_DATA_SOURCE = 1007;
    public static final int PREFERENCES_DISPLAY = 1008;
    public static final int PREFERENCES_MAP_TRAY = 1009;
    public static final int PREFERENCES_CAROUSEL = 1010;
    public static final int PREFERENCES_AUDIO = 1011;
    public static final int PREFERENCES_ADVANCE = 1012;
    public static final int PREFERENCES_SHARE_TRAFFIC = 1013;
    public static final int PREFERENCES_SHARE_SETTINGS = 1014;
    public static final int PREFERENCES_RESET = 1015;
    public static final int PREFERENCES_END = 1016;


    //main menu views
    public static final int ABOUT_VIEW = 1100;
/*
    SC_ViewType_DevTools,                               !< DevTool 


    //Subscription View
    SC_ViewType_SubscriptionStart = 1200,
    SC_ViewType_MyAccountView,                          !< MyAccountView 
    SC_ViewType_PurchaseView,                           !< PurchaseView   
    SC_ViewType_PurchasePriceView,                      !< PurchasePriceView   
    SC_ViewType_3DCitiesSetup,                          !< 3DCitiesSetupView   
    SC_ViewType_SubscriptionEnd,
    
    // ASR views
    SC_ViewType_AsrPlaceView,                           !< Asr say place view. 
    SC_ViewType_AsrAirportView,                         !< Asr say airport view. 
    SC_ViewType_AsrFullAddressView,                     !< Asr say full-address view. 
    SC_ViewType_AsrAddressView,                         !< Asr say address(split 4 fields) view. 
    SC_ViewType_AsrDisambiguateView,                    !< Asr disambiguate list view. 
    
    // Roadside assistance view
    SC_ViewType_RoadsideAssistanceView,

    //MDN verification views
    SC_ViewType_MdnVerificationViewStart,       
    SC_ViewType_MdnInputView,                           !< Mdn input view 
    SC_ViewType_PinInputView,                           ! < User PIN input view 
    SC_ViewType_MdnVerificationViewEnd
*/    
}
