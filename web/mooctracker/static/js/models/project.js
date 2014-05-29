// global backbone app
var app = app || {};

(function(){
  'use strict';

  // the course model

  app.Course = Backbone.Model.extend({
    defaults: {
      name: 'Unknown Project',
      description: 'Project description',
      projectUrl: '#',
      githubUrl: '#'
    }
  });


})();