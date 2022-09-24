import jenkins.model.*;
def disableJob(){
    
if(env.ACTION == 'Post_Codefreez')
{
	if(env.VERSION == '16' || env.VERSION == '17')
	{
	//Disable promotion to mr-release
	def job = hudson.model.Hudson.instance.getJob("DELIVER_MR" +version+ "-PROMOTION_TO_MR" +version+ "-RELEASE")
	println("Job has been Disabled: " +job)
    //Enable promote to shadow_promote  
	def job1 = hudson.model.Hudson.instance.getJob("DELIVER_PROMOTE-" +version+ "_TO_SHADOW_PROMOTE_" +version)
	println("Job has been Enabled: " +job1)
    //Enable promote to mr-release  
	def job2 = hudson.model.Hudson.instance.getJob("DELIVER_PROMOTE-" +version+ "_TO_MR" +version+ "-RELEASE")
	println("Job has been Enabled: " +job2)
    //Enable Rebase from mr-promotion to SHADOW_PROMOTE   
	def jenkins = Jenkins.instance
    def job3 = jenkins.getItemByFullName("REBASE-MULTIBRANCH/SHADOW_PROMOTE_" +version)
    println("Job has been Enabled: " +job3)
    
    //Enable Deliver from SHADOW_PROMOTE to mr-promotion   
	//def jenkins = Jenkins.instance
    def job4 = jenkins.getItemByFullName("DELIVER-MULTIBRANCH/SHADOW_PROMOTE_" +version)
    println("Job has been Enabled: " +job4)
  
	try {
		job.disabled=true
  		job.save()
  		println(job.name)
        job1.disabled=false
  		job1.save()
  		println(job1.name)
  	
    	job2.disabled=false
  		job2.save()
  		println(job2.name)
    
  		job3.disabled=false
  		job3.save()
  	    println(job3.name)
      
        job4.disabled=false
  		job4.save()
  	    println(job4.name)
  	
		} catch (Exception ex) {
           	println("Error disabling jobname. " + ex.getMessage())
    		}  
    	} 
 } 
if(env.ACTION == 'Post_Release')
	{
    	if(env.VERSION == '16' || env.VERSION == '17')
          {
          //Enable promotion to mr-release
          def job = hudson.model.Hudson.instance.getJob("DELIVER_MR" +version+ "-PROMOTION_TO_MR" +version+ "-RELEASE")
          println("Job has been Enabled: " +job)
          
          //Disable promote to shadow_promote  
	      def job1 = hudson.model.Hudson.instance.getJob("DELIVER_PROMOTE-" +version+ "_TO_SHADOW_PROMOTE_" +version)
	      println("Job has been Disabled: " +job1)
      
          //Disable promote to mr-release  
          def job2 = hudson.model.Hudson.instance.getJob("DELIVER_PROMOTE-" +version+ "_TO_MR" +version+ "-RELEASE")
          println("Job has been Disabled: " +job2)
      
          //Disable Rebase from mr16-promotion to SHADOW_PROMOTE_  
          def jenkins = Jenkins.instance
          def job3 = jenkins.getItemByFullName("REBASE-MULTIBRANCH/SHADOW_PROMOTE_" +version)
          println("Job has been Disabled: " +job3)
              
          //Disabled Deliver from SHADOW_PROMOTE to mr-promotion   
          //def jenkins = Jenkins.instance
          def job4 = jenkins.getItemByFullName("DELIVER-MULTIBRANCH/SHADOW_PROMOTE_" +version)
          println("Job has been Disabled: " +job4)
        
          try {
              job.disabled=false
              job.save()
              println(job.name)
              job1.disabled=true
              job1.save()
              println(job1.name)
          
              job2.disabled=true
              job2.save()
              println(job2.name)
          
              job3.disabled=true
              job3.save()
              println(job3.name)
            
              job4.disabled=true
              job4.save()
              println(job4.name)
          
              } catch (Exception ex) {
                  println("Error disabling jobname. " + ex.getMessage())
                  }
		}
    }
} 
return this 
