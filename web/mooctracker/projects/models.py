from django.db import models

class Project(models.Model):
  project_name = models.CharField(max_length = 200)
  url = models.URLField(max_length=400)