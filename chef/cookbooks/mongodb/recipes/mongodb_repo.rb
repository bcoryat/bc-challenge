#include_recipe "yum::yum"

  execute "yum clean all" do
   action :nothing
  end

  yum_repository  "mongo_repo" do
    description "Mongo RPM Repository"
    url "http://downloads-distro.mongodb.org/repo/redhat/os/x86_64/"
    action :add
    notifies :run, "execute[yum clean all]", :immediately
  end